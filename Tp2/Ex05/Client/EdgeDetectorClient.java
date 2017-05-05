package Tp2.Ex05.Client;

import Tp2.Ex05.Common.IEdgeDetectorService;
import Tp2.Ex05.Common.ImageChunkHandler;
import Tp2.Ex05.Common.ImageSerializable;
import Tp2.Ex05.Common.SobelEdgeDetector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:57
 */
public class EdgeDetectorClient {


    private HashMap<Integer, Registry> registries;
    private static final int TOTAL_PORTS_AVAILABLE = 4;

    // Will split image in 4 parts: 2 rows and 2 cols
    public static final int IMAGE_ROWS = 2;
    public static final int IMAGE_COLS = 2;
    public static final int REDUNDANT_PIXELS = 1;

    // Many services will be called to apply sobel filter
    private HashMap<Integer, Runnable> runnables;
    private HashMap<Integer, Thread> threads;
    private ArrayList<Integer> portsAvailable;

    public EdgeDetectorClient(String host, int startingPort){
        this.registries = new HashMap<Integer, Registry>();
        this.portsAvailable = new ArrayList<Integer>();

        int currentPort;
        for (int i=0; i < TOTAL_PORTS_AVAILABLE; i++){
            currentPort = startingPort + i;
            portsAvailable.add(currentPort);
            System.out.println("Connecting to port: " + currentPort);
            try {
                Registry r = LocateRegistry.getRegistry(host, currentPort);
                this.registries.put(currentPort, r);
            } catch (RemoteException e) {
                System.out.println("CATCHED IN: registries, port: " + currentPort);
                e.printStackTrace();
            }
        }

        this.runnables = new HashMap<Integer, Runnable>();
        this.threads = new HashMap<Integer, Thread>();
    }

    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException {
        return new ImageSerializable(this.callEdgeDetector(image.getBufferedImage()));
    }
    public BufferedImage detectEdges(BufferedImage image) throws RemoteException {
        return this.callEdgeDetector(image);
    }

    private BufferedImage callEdgeDetector(BufferedImage originalImage) throws RemoteException {
        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(IMAGE_ROWS, IMAGE_COLS, REDUNDANT_PIXELS, originalImage);

        // Original image data displayed:
        System.out.println("Original Image: " + originalImage.getWidth() + "x" + originalImage.getHeight());

        // Splits image:
        BufferedImage[] imageChunks = new BufferedImage[0];
        try {
            imageChunks = imageChunkHandler.split();
        } catch (IOException e) {e.printStackTrace();}

        // Instantiating threads and runnables
        this.prepareImageThreads(imageChunks);

        // Starts threads
        this.startAllThreads();

        // Wait for all of them to finish
        this.joinAllThreads();

        System.out.println("All threads finished");

        // Retrieve results from each thread
        int maxPixelValue = this.maxPixelValueFromThreads();
        ArrayList<int[][]> pixelsChunks = this.pixelsArraysFromThreads();

        // Normalize and convert to BufferedImage
        pixelsChunks = this.normalizePixelsValues(pixelsChunks, maxPixelValue);
        BufferedImage[] toJoin = this.toBufferedImage(pixelsChunks);

        // Construct final image from chunks
        BufferedImage finalImage = imageChunkHandler.join(toJoin);

        return finalImage;
    }

    private BufferedImage[] toBufferedImage(ArrayList<int[][]> pixelsChunks) {
        BufferedImage[] out = new BufferedImage[pixelsChunks.size()];
        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector();
        for (int i=0; i < pixelsChunks.size(); i++) {
            int[][] thisPixelsChunk = pixelsChunks.get(i);
            out[i] = sobelEdgeDetector.toBufferedImage(thisPixelsChunk);
        }
        return out;
    }

    private ArrayList<int[][]> normalizePixelsValues(ArrayList<int[][]> pixelsChunks, int maxPixelValue) {
        System.out.println("Normalizing image...");
        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector();
        ArrayList<int[][]> out = new ArrayList<int[][]>();
        for (int i=0; i < pixelsChunks.size(); i++) {
            int[][] thisPixelsChunk = pixelsChunks.get(i);
            out.add(sobelEdgeDetector.normalize(thisPixelsChunk, maxPixelValue));
        }
        return out;

    }

    private ArrayList<int[][]> pixelsArraysFromThreads() {
        ArrayList<int[][]> pixelsChunks = new ArrayList<int[][]>();
        for (int i=0; i <runnables.size(); i++){
            EdgeDetectorRunnable edgeDetectorRunnable = (EdgeDetectorRunnable) runnables.get(i);
            pixelsChunks.add(edgeDetectorRunnable.getFinalPixelValues());
        }
        return pixelsChunks;
    }

    private int maxPixelValueFromThreads() {
        int maxPixelValue = 0;
        for (int i=0; i < this.runnables.size(); i++){
            EdgeDetectorRunnable edgeDetectorRunnable = (EdgeDetectorRunnable) this.runnables.get(i);
            int thisMaxPixel = edgeDetectorRunnable.getMaxPixelValue();
            if (thisMaxPixel > maxPixelValue) maxPixelValue = thisMaxPixel;
        }
        return maxPixelValue;
    }

    private void prepareImageThreads(BufferedImage[] images) {
        // 'i' will stand for:
        // - thread id,
        // - image chunck id and
        // - runnable id.
        int portPosition = 0;
        for (int i=0; i < images.length; i++) {
            BufferedImage chunk = images[i];
            IEdgeDetectorService service = null;

            boolean connected = false;
            boolean serverIsUp = true;
            int portPos = portPosition;
            int portToConnect;
            while (!connected && serverIsUp){
                portToConnect = this.portsAvailable.get(portPos);
                try {
                    service = this.getService(portToConnect);
                    connected = true;
                } catch (RemoteException e) {
                    System.out.println("Se cayo servicio en puerto: " + portToConnect);
                    if (++portPos == this.portsAvailable.size())
                        portPos = 0;
                    if (portPos == portPosition)
                        serverIsUp = false;

                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }

            if (!serverIsUp) {
                System.out.println("Server is down: No available ports to connect to");
                System.out.println("Please, try again later.");
                System.exit(1);
            }

            Runnable r = new EdgeDetectorRunnable(service, chunk, i);
            Thread t = new Thread(r);
            this.runnables.put(i, r);
            this.threads.put(i, t);

            if (++portPosition == this.portsAvailable.size())
                portPosition = 0;
        }
    }

    private IEdgeDetectorService getService(int port) throws RemoteException, NotBoundException {
        String dns = IEdgeDetectorService.DNS_NAME;
        Registry r = this.registries.get(port);
        IEdgeDetectorService service = (IEdgeDetectorService) r.lookup(dns);
        return service;
    }

    private void startAllThreads() {
        for (int i=0; i <threads.size(); i++) this.threads.get(i).start();
    }

    private void joinAllThreads() {
        for (int i=0; i <threads.size(); i++) try {this.threads.get(i).join();} catch (InterruptedException e) {e.printStackTrace();}
    }

}

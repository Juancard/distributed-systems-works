package Tp2.Ex04.Client;

import Tp2.Ex04.Common.IEdgeDetectorService;
import Tp2.Ex04.Common.ImageChunkHandler;
import Tp2.Ex04.Common.ImageSerializable;
import Tp2.Ex04.Common.SobelEdgeDetector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    private Registry registry;

    // Will split image in 4 parts: 2 rows and 2 cols
    public static final int IMAGE_ROWS = 4;
    public static final int IMAGE_COLS = 1;
    public static final int REDUNDANT_PIXELS = 1;

    // Many services will be called to apply sobel filter
    private ArrayList<IEdgeDetectorService> iEdgeDetectorServices;
    private HashMap<Integer, Runnable> runnables;
    HashMap<Integer, Thread> threads;

    public EdgeDetectorClient(String host, int port) throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry(host, port);

        this.iEdgeDetectorServices = new ArrayList<IEdgeDetectorService>();
        this.runnables = new HashMap<Integer, Runnable>();
        this.threads = new HashMap<Integer, Thread>();

        this.connectToServices();
    }

    private void connectToServices() throws RemoteException, NotBoundException {
        String currentDns = "";
        for (int i=1; i <= IMAGE_COLS * IMAGE_ROWS; i++){

            try {
                currentDns = (String) IEdgeDetectorService.class.getField("DNS_NAME_" + i).get(null);
            } catch (IllegalAccessException e) {e.printStackTrace();} catch (NoSuchFieldException e) {e.printStackTrace();}

            IEdgeDetectorService service = (IEdgeDetectorService) this.registry.lookup(currentDns);

            this.iEdgeDetectorServices.add(service);
        }
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
        for (int i=0; i < images.length; i++) {
            BufferedImage chunk = images[i];
            IEdgeDetectorService service = this.iEdgeDetectorServices.get(i);
            Runnable r = new EdgeDetectorRunnable(service, chunk, i);
            Thread t = new Thread(r);
            this.runnables.put(i, r);
            this.threads.put(i, t);
        }
    }

    private void startAllThreads() {
        for (int i=0; i <threads.size(); i++) this.threads.get(i).start();
    }

    private void joinAllThreads() {
        for (int i=0; i <threads.size(); i++) try {this.threads.get(i).join();} catch (InterruptedException e) {e.printStackTrace();}
    }

}

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
    public static final int IMAGE_ROWS = 2;
    public static final int IMAGE_COLS = 2;
    public static final int REDUNDANT_PIXELS = 1;

    // Wil call four processes
    private ArrayList<IEdgeDetectorService> iEdgeDetectorServices;

    public EdgeDetectorClient(String host, int port) throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry(host, port);
        this.iEdgeDetectorServices = new ArrayList<IEdgeDetectorService>();
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
        //return this.iEdgeDetectorService3.detectEdges(image);
        return null;
    }
    public BufferedImage detectEdges(BufferedImage image) throws RemoteException {
        //return this.iEdgeDetectorService3.detectEdges(new ImageSerializable(image)).getBufferedImage();
        return this.callEdgeDetector(image);
    }

    private BufferedImage callEdgeDetector(BufferedImage originalImage) throws RemoteException {
        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(IMAGE_ROWS, IMAGE_COLS, REDUNDANT_PIXELS, originalImage);

        // Original image data displayed:
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        System.out.println("Original Image: " + width + "x" + height);

        // Splits image:
        BufferedImage[] imageChunks = new BufferedImage[0];
        try {
            imageChunks = imageChunkHandler.split();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call threads
        HashMap<Integer, Runnable> runnables = new HashMap<Integer, Runnable>();
        HashMap<Integer, Thread> threads = new HashMap<Integer, Thread>();

        for (int i = 0; i < imageChunks.length; i++){
            BufferedImage chunk = imageChunks[i];
            IEdgeDetectorService service = this.iEdgeDetectorServices.get(i);
            Runnable r = new EdgeDetectorRunnable(service, chunk, i);
            Thread t = new Thread(r);
            runnables.put(i, r);
            threads.put(i, t);
        }

        // Starts threads
        for (int i=0; i <threads.size(); i++) threads.get(i).start();

        System.out.println("Before joining");

        // Wait for them to finish
        for (int i=0; i <threads.size(); i++) try {threads.get(i).join();} catch (InterruptedException e) {e.printStackTrace();}

        System.out.println("All threads finished");

        // Retrieve results from each thread
        ArrayList<int[][]> pixelsChunks = new ArrayList<int[][]>();

        int maxPixelValue = 0;
        for (int i=0; i <runnables.size(); i++){
            EdgeDetectorRunnable edgeDetectorRunnable = (EdgeDetectorRunnable) runnables.get(i);
            int thisMaxPixel = edgeDetectorRunnable.getMaxPixelValue();
            if (thisMaxPixel > maxPixelValue) maxPixelValue = thisMaxPixel;
            pixelsChunks.add(edgeDetectorRunnable.getFinalPixelValues());
        }

        // Normalize and convert to BufferedImage
        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector();
        BufferedImage[] toJoin = new BufferedImage[pixelsChunks.size()];
        for (int i=0; i < pixelsChunks.size(); i++) {
            int[][] thisPixelsChunk = pixelsChunks.get(i);
            thisPixelsChunk = sobelEdgeDetector.normalize(thisPixelsChunk, maxPixelValue);
            toJoin[i] = sobelEdgeDetector.toBufferedImage(thisPixelsChunk);
            File f = new File("lala" + i + ".jpg");
            try {
                ImageIO.write(toJoin[i], "jpg", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Construct final image from chunks
        BufferedImage finalImage = imageChunkHandler.join(toJoin);
        return finalImage;
    }
}

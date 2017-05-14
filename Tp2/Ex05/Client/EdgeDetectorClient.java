package Tp2.Ex05.Client;

import Common.ServerInfo;
import Tp2.Ex05.Common.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:57
 */
public class EdgeDetectorClient {

    // Many services will be called to apply sobel filter
    private HashMap<Integer, Runnable> runnables;
    private HashMap<Integer, Thread> threads;
    private RemotePortsManager remotePortsManager;

    public EdgeDetectorClient(BufferedReader remotePortsReader) throws NoPortsAvailableException, IOException {
        this.runnables = new HashMap<Integer, Runnable>();
        this.threads = new HashMap<Integer, Thread>();

        ArrayList<ServerInfo> remotePorts = this.loadRemotePorts(remotePortsReader);
        this.remotePortsManager = new RemotePortsManager(remotePorts);
    }

    private ArrayList<ServerInfo> loadRemotePorts(BufferedReader remotePortsReader) throws NoPortsAvailableException, IOException {
        ArrayList<ServerInfo> remotePorts = RemotePortsLoader.remotePortsFrom(remotePortsReader);
        if (remotePorts.isEmpty())
            throw new NoPortsAvailableException("Loading ports - No ports read from buffer reader.");
        return remotePorts;
    }

    public BufferedImage detectEdges(BufferedImage image, int rowsToSplitImage, int colsToSplitImage, int redundantPixels) throws RemoteException, NoPortsAvailableException {
        return this.callEdgeDetector(
                image,
                rowsToSplitImage,
                colsToSplitImage,
                redundantPixels
        );
    }

    private BufferedImage callEdgeDetector(BufferedImage originalImage, int rowsToSplitImage, int colsToSplitImage, int redundantPixels) throws RemoteException, NoPortsAvailableException {
        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(rowsToSplitImage, colsToSplitImage, redundantPixels, originalImage);

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

    private void prepareImageThreads(BufferedImage[] images) throws NoPortsAvailableException {
        // 'i' will stand for:
        // - thread id,
        // - image chunck id and
        // - runnable id.
        for (int i=0; i < images.length; i++) {
            BufferedImage chunk = images[i];
            IEdgeDetectorService service = this.remotePortsManager.getService();

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

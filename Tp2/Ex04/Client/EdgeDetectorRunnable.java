package Tp2.Ex04.Client;

import Tp2.Ex04.Common.IEdgeDetectorService;
import Tp2.Ex04.Common.SobelEdgeDetector;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 28/04/17
 * Time: 13:42
 */
public class EdgeDetectorRunnable implements Runnable {
    private int id;
    private IEdgeDetectorService service;
    private BufferedImage originalImage;
    private SobelEdgeDetector sobelEdgeDetector;

    private int maxPixelValue;
    private int[][] finalPixelValues;

    public EdgeDetectorRunnable (IEdgeDetectorService service, BufferedImage image, int id){
        this.id = id;
        this.service = service;
        this.originalImage = image;
        this.sobelEdgeDetector = new SobelEdgeDetector();

        // Filled by runnable
        this.maxPixelValue = 0;
        this.finalPixelValues = null;
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("Running (%d):%dx%d", this.id, originalImage.getWidth(), originalImage.getHeight()));
            this.finalPixelValues = service.detectEdges(sobelEdgeDetector.toPixelArray2d(this.originalImage));
            this.maxPixelValue = this.sobelEdgeDetector.getMaxPixelValue(this.finalPixelValues);
            System.out.println(String.format("Finished (%d):%dx%d", this.id, originalImage.getWidth(), originalImage.getHeight()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    // GETTERS AND SETTERS

    public IEdgeDetectorService getService() {
        return service;
    }

    public void setService(IEdgeDetectorService service) {
        this.service = service;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public int getMaxPixelValue() {
        return maxPixelValue;
    }

    public void setMaxPixelValue(int maxPixelValue) {
        this.maxPixelValue = maxPixelValue;
    }

    public int[][] getFinalPixelValues() {
        return finalPixelValues;
    }

    public void setFinalPixelValues(int[][] finalPixelValues) {
        this.finalPixelValues = finalPixelValues;
    }

    public int getId() {
        return id;
    }
}

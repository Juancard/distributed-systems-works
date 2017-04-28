package Tp2.Ex04.Server;

import Tp2.Ex04.Common.IEdgeDetectorService;
import Tp2.Ex04.Common.ImageChunkHandler;
import Tp2.Ex04.Common.ImageSerializable;
import Tp2.Ex04.Common.SobelEdgeDetector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:53
 */
public class EdgeDetectorService implements IEdgeDetectorService{

    // Will split image in 4 parts: 2 rows and 2 cols
    public static final int IMAGE_ROWS = 2;
    public static final int IMAGE_COLS = 2;
    public static final int REDUNDANT_PIXELS = 1;

    private SobelEdgeDetector sobelEdgeDetector;
    private String id;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}


    public EdgeDetectorService(){
        this.sobelEdgeDetector = new SobelEdgeDetector();
    }

    @Override
    public int[][] detectEdges(int[][] pixelsValues) throws RemoteException {
        this.log("In detect edges to image: " + pixelsValues.length + "x" + pixelsValues[0].length);
        return sobelEdgeDetector.getPixelValuesEdged(pixelsValues);
    }

    @Override
    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException {
        BufferedImage originalImage = image.getBufferedImage();

        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(IMAGE_ROWS, IMAGE_COLS, REDUNDANT_PIXELS, originalImage);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        this.log("Original Image: " + width + "x" + height);

        BufferedImage[] imageChunks = new BufferedImage[0];
        try {
            imageChunks = imageChunkHandler.split();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<int[][]> pixels = new ArrayList<int[][]>();
        int maxPixelValue = 0;
        for (int i = 0; i < imageChunks.length; i++) {
            BufferedImage thisChunk = imageChunks[i];
            this.log(String.format("Image %s: %dx%d", (i+1), thisChunk.getWidth(), thisChunk.getHeight()));

            int[][] thisPixels = sobelEdgeDetector.getPixelValuesEdged(thisChunk);
            pixels.add(i, thisPixels);

            int maxValue = sobelEdgeDetector.getMaxPixelValue(thisPixels);
            maxPixelValue = (maxValue > maxPixelValue)? maxValue : maxPixelValue;
        }

        for (int i = 0; i < imageChunks.length; i++) {
            int[][] pixelsNormalized = sobelEdgeDetector.normalize(pixels.get(i), maxPixelValue);
            imageChunks[i] = sobelEdgeDetector.toBufferedImage(pixelsNormalized);
        }


        BufferedImage finalImage = imageChunkHandler.join(imageChunks);
        this.log("Final Image: " + finalImage.getWidth() + "x" + finalImage.getHeight());

        return new ImageSerializable(finalImage);
    }


    public void log(String message){
        System.out.println(this.id + ": " + message);
    }
}

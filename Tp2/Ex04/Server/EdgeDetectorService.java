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

    @Override
    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException {
        BufferedImage originalImage = image.getBufferedImage();

        File f = new File("lala.jpg");
        try {
            ImageIO.write(originalImage, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector();


        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(IMAGE_ROWS, IMAGE_COLS, REDUNDANT_PIXELS, originalImage);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        System.out.println("Original Image: " + width + "x" + height);

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
            System.out.println(String.format("Image %s: %dx%d", (i+1), thisChunk.getWidth(), thisChunk.getHeight()));

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
        System.out.println("Final Image: " + finalImage.getWidth() + "x" + finalImage.getHeight());

        return new ImageSerializable(finalImage);
    }
}

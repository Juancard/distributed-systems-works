package Tp2.Ex04;

import Common.CommonMain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class MainMenu {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5024;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 4;
    private static final String EXERCISE_TITLE = "Sobel Operator";

    private static final String IMAGES_PATH = "distributed-systems-works/Tp2/Ex04/Resources/Images/";

    private Scanner sc = new Scanner(System.in);
    private String host;
    private int port;

    public static void main(String[] args) throws IOException {
        MainMenu mainMenu = new MainMenu(DEFAULT_HOST, DEFAULT_PORT);
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        mainMenu.start();
    }

    public MainMenu(String defaultHost, int defaultPort){
        newClient(defaultHost, defaultPort);
    }

    private void newClient(String defaultHost, int defaultPort) {
        this.host = CommonMain.askForHost(defaultHost);
        this.port = CommonMain.askForPort(defaultPort);
    }

    public void start() throws IOException {
        String imageUrl = askForImage();
        URL url = new URL(imageUrl);
        BufferedImage originalImage = ImageIO.read(url);

        SobelEdgeDetector sobelEdgeDetector = new SobelEdgeDetector();

        // Will split originalImage in 4 parts: 2 rows and 2 cols
        final int IMAGE_ROWS = 1;
        final int IMAGE_COLS = 2;
        final int REDUNDANT_PIXELS = 0;
        ImageChunkHandler imageChunkHandler = new ImageChunkHandler(IMAGE_ROWS, IMAGE_COLS, REDUNDANT_PIXELS);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        System.out.println("Original Image: " + width + "x" + height);

        BufferedImage[] imageChunks = imageChunkHandler.split(originalImage);
        ArrayList<int[][]> pixels = new ArrayList<int[][]>();
        int maxPixelValue = 0;
        for (int i = 0; i < imageChunks.length; i++) {
            BufferedImage thisChunk = imageChunks[i];
            System.out.println(String.format("Image %s: %dx%d", (i+1), thisChunk.getWidth(), thisChunk.getHeight()));

            //File f = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + (i+1) + "." + this.getFilenameExtension(imageUrl));
            //ImageIO.write(imageChunks[i], this.getFilenameExtension(imageUrl), f);

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

        File f = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + "_super_cutted" + "." + this.getFilenameExtension(imageUrl));
        ImageIO.write(finalImage, this.getFilenameExtension(imageUrl), f);

        System.exit(1);

        /*
        BufferedImage image1 = new BufferedImage(CHUNK_WIDTH, CHUNK_HEIGHT, originalImage.getType());
        image1.createGraphics().drawImage(originalImage, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, null);

        BufferedImage image2 = new BufferedImage(CHUNK_WIDTH, CHUNK_HEIGHT, originalImage.getType());
        image2.createGraphics().drawImage(originalImage, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, width / 2 - REDUNDANT_PIXELS, 0, width, height, null);

        System.out.println("Original originalImage: " + width + "x" + height);
        System.out.println("Image 1: " + image1.getWidth() + "x" + image1.getHeight());
        System.out.println("Image 2: " + image2.getWidth() + "x" + image2.getHeight());

        int[][] pixels = sobelEdgeDetector.getPixelValuesEdged(originalImage);
        int[][] pixels1 = sobelEdgeDetector.getPixelValuesEdged(image1);
        int[][] pixels2 = sobelEdgeDetector.getPixelValuesEdged(image2);

        int maxValue = sobelEdgeDetector.getMaxPixelValue(pixels);
        int maxValue1 = sobelEdgeDetector.getMaxPixelValue(pixels1);
        int maxValue2 = sobelEdgeDetector.getMaxPixelValue(pixels2);
        int maxPixelValue = (maxValue1 > maxValue2)? maxValue1 : maxValue2;

        pixels = sobelEdgeDetector.normalize(pixels, maxPixelValue);
        pixels1 = sobelEdgeDetector.normalize(pixels1, maxPixelValue);
        pixels2 = sobelEdgeDetector.normalize(pixels2, maxPixelValue);

        originalImage = sobelEdgeDetector.toBufferedImage(pixels);
        image1 = sobelEdgeDetector.toBufferedImage(pixels1);
        image2 = sobelEdgeDetector.toBufferedImage(pixels2);

        // Remove redundant pixel
        BufferedImage finalImage1 = new BufferedImage(CHUNK_WIDTH - REDUNDANT_PIXELS, height, image1.getType());
        finalImage1.createGraphics().drawImage(image1, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, null);

        // Remove redundant pixel
        BufferedImage finalImage2 = new BufferedImage(CHUNK_WIDTH - REDUNDANT_PIXELS, height, image2.getType());
        finalImage2.createGraphics().drawImage(image2, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, REDUNDANT_PIXELS, 0, CHUNK_WIDTH, height, null);

        // Construct originalImage with chunk images
        BufferedImage finalImg = new BufferedImage(width, height, image1.getType());
        finalImg.createGraphics().drawImage(finalImage1, 0, 0, null);
        finalImg.createGraphics().drawImage(finalImage2, width / 2, 0, null);

        System.out.println("Final originalImage: " + finalImg.getWidth() + "x" + finalImg.getHeight());

        File ff = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + "_edged" + "." + this.getFilenameExtension(imageUrl));
        ImageIO.write(originalImage, this.getFilenameExtension(imageUrl), ff);

        File f = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + "_cutted" + "." + this.getFilenameExtension(imageUrl));
        ImageIO.write(finalImg, this.getFilenameExtension(imageUrl), f);
         */
    }

    private String filenameFromPath(String filename) {
        return filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf("."));
    }

    private String getFilenameExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    private String askForImage() {
        String DEFAULT = "http://tutorialspoint.com/java_dip/images/digital_image_processing.jpg";
        //DEFAULT = "https://www.tutorialspoint.com/java_dip/images/grayscale.jpg";
        //DEFAULT = "http://www.smalljpg.com/temp/20170424223049.jpg";
        //DEFAULT = "http://www.smalljpg.com/temp/20170424224711.jpg";
        //DEFAULT = "https://s29.postimg.org/kjex7dx6f/300px-_Valve_original_1.png";
        //DEFAULT = "http://4.bp.blogspot.com/_6ZIqLRChuQg/TF0-bhL6zoI/AAAAAAAAAoE/56OJXkRAFz4/s1600/lenaOriginal.png";

        System.out.print("Enter image url: ");
        String imageUrl = sc.nextLine();

        return (imageUrl.isEmpty())? DEFAULT : imageUrl;
    }

}

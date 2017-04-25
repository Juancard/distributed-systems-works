package Tp2.Ex04;

import Common.CommonMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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
        String filename = this.filenameFromPath(imageUrl);
        URL url = new URL(imageUrl);

        BufferedImage image = ImageIO.read(url);

        // Grey Scale needed for sobel operator to work better
        this.toGreyScale(image);

        // White background for .png images
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        image = newBufferedImage;

        int[][] pixels = new int[image.getWidth()][image.getHeight()];
        for( int i = 0; i < image.getWidth(); i++ )
            for( int j = 0; j < image.getHeight(); j++ )
                pixels[i][j] = image.getRGB( i, j );


        int[][] result = this.sobelOperator(pixels);

        for( int i = 0; i < image.getWidth(); i++ )
            for( int j = 0; j < image.getHeight(); j++ )
                image.setRGB(i, j, result[i][j]);


        File f = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + "_sobel_operator" + "." + this.getFilenameExtension(imageUrl));
        ImageIO.write(image, this.getFilenameExtension(imageUrl), f);


    }

    private int[][] sobelOperator(int[][] input) {
        int width = input.length;
        int height = input[0].length;
        int[][] sobelValues = new int[width][height];
        int[][] output = new int[width][height];

        int[][] kernelGX = new int[][]{
                { -1, 0, 1 },
                { -2, 0, 2 },
                { -1, 0, 1 }
        };
        int[][] kernelGY = new int[][]{
                { -1, -2, -1 },
                { 0, 0, 0 },
                { 1, 2, 1 }
        };
        int kernelSize = 3;

        int accumulatorGX, accumulatorGY;
        int maxToNormalize = 0;
        int neighborRow, neighborCol, valueImage, valueKernelGX, valueKernelGY;
        for (int imageRow = (kernelSize - 1) / 2; imageRow <= width - (kernelSize + 1) / 2; imageRow++) {
            for (int imageCol = (kernelSize - 1) / 2; imageCol <= height - (kernelSize + 1) / 2; imageCol++) {
                //System.out.println(String.format("Pixel: (%d, %d) = %d", imageRow, imageCol, input[imageRow][imageCol]));
                accumulatorGX = 0;
                accumulatorGY = 0;

                for (int kernelX = 0; kernelX < kernelSize; kernelX++){
                    for (int kernelY = 0; kernelY < kernelSize; kernelY++){
                        neighborRow = imageRow + -1 + kernelX;
                        neighborCol = imageCol + -1 + kernelY;
                        valueImage = (new Color(input[neighborRow][neighborCol])).getBlue();
                        valueKernelGX = kernelGX[kernelX][kernelY];
                        valueKernelGY = kernelGY[kernelX][kernelY];
                        //System.out.println(String.format("-- (%d, %d) = %d", neighborRow, neighborCol, valueImage));
                        accumulatorGX += valueImage * valueKernelGX;
                        accumulatorGY += valueImage * valueKernelGY;
                        //System.out.println(valueImage + " * " + valueKernel);
                    }
                }

                double GX2 = Math.pow(accumulatorGX, 2);
                double GY2 = Math.pow(accumulatorGY, 2);
                int sobelValue = (int) Math.sqrt(GX2 + GY2);

                if (sobelValue > maxToNormalize) {
                    maxToNormalize = sobelValue;
                }

                sobelValues[imageRow][imageCol] = sobelValue;
                //int valueRgb = (valueNoRgb<<16 | valueNoRgb<<8 | valueNoRgb);

                //output[imageRow][imageCol] = valueRgb;
            }
            //System.out.println();
        }

        float ratio=(float) maxToNormalize/255;
        for (int i=0; i < sobelValues.length; i++){
            for (int j=0; j < sobelValues[i].length; j++){
                int sobelValue = (int)(sobelValues[i][j] / ratio);
                int valueRgb = (sobelValue<<16 | sobelValue<<8 | sobelValue);

                output[i][j] = valueRgb;
            }
        }

        return output;
    }

    private void toGreyScale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int rgbColor, alpha, red, green, blue, avg, newRgbColor;
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                rgbColor = image.getRGB(x, y);
                alpha = (rgbColor>>24)&0xff;
                red = (rgbColor>>16)&0xff;
                green = (rgbColor>>8)&0xff;
                blue = rgbColor&0xff;

                avg = (red + green + blue)/3;

                newRgbColor = (alpha<<24) | (avg<<16) | (avg<<8) | avg;
                image.setRGB(x, y, newRgbColor);
            }
        }
    }

    private String filenameFromPath(String filename) {
        return filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf("."));
    }

    private String getFilenameExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    /* NOT WORKING ON SYSTE,.LOADLIBRARY
    private void sobelOperatorOpenCv(String filename){
        int kernelSize = 9;
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        Mat source = Imgcodecs.imread(IMAGES_PATH + filename,  Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());

        Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F){
            {
                put(0,0,-1);
                put(0,1,0);
                put(0,2,1);

                put(1,0-2);
                put(1,1,0);
                put(1,2,2);

                put(2,0,-1);
                put(2,1,0);
                put(2,2,1);
            }
        };

        Imgproc.filter2D(source, destination, -1, kernel);
        Imgcodecs.imwrite(IMAGES_PATH + "sobel_" + filename, destination);
    }
    */

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

    private void saveImage(URL imageUrl, String filename) throws IOException {
        CommonMain.display("Downloading File From: " + imageUrl);

        InputStream inputStream = imageUrl.openStream();
        OutputStream outputStream = new FileOutputStream(IMAGES_PATH + filename);
        byte[] buffer = new byte[2048];

        int length = 0;

        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }

}

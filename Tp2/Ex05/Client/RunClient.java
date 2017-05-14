package Tp2.Ex05.Client;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex05.Common.NoPortsAvailableException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class RunClient {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex05/Resources/Config/config.properties";
    private static final String FILENAME_SUFFIX = "_sobel_filter";

    private Scanner sc = new Scanner(System.in);
    private EdgeDetectorClient edgeDetectorClient;
    private Path imagesPath;

    private BufferedImage originalImage;
    private String originalImageUrl;

    private int rowsToSplitImage;
    private int colsToSplitImage;
    private int redundantPixels;

    public static void main(String[] args) throws IOException {

        try {
            Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);

            if (PropertiesManager.propIsPath(properties, "IMAGES_PATH"))
                throw new Exception("Images Path is not a valid directory");
            if (PropertiesManager.propIsFileExists(properties, "REMOTE_PORTS_FILE"))
                throw new Exception("Remote Ports file does not exists");


            String imagesPath = properties.getProperty("IMAGES_PATH");
            String remotePortsFile = properties.getProperty("REMOTE_PORTS_FILE");

            RunClient runClient = new RunClient(imagesPath, remotePortsFile);
            runClient.start();
        } catch (Exception e) {
            CommonMain.display("Error: " + e.getMessage());
            System.exit(1);
        }


    }

    public RunClient(String imagesPathString, String remotePortsFileString) throws IOException, NoPortsAvailableException {
        this.imagesPath = this.setPathFromString(imagesPathString);
        this.edgeDetectorClient = new EdgeDetectorClient(
            new BufferedReader(
                    new FileReader(
                            remotePortsFileString
                    )
            )
        );
    }

    private Path setPathFromString(String pathString) throws IOException {
        File f = new File(pathString);

        if (!(f.isDirectory()))
            throw new IOException("Images path is not a valid directory");
        if (!(f.exists()) && !(f.mkdir()))
            throw new IOException("Failed creating images directory at " + f.getPath());

        return f.toPath();
    }


    public void start() throws IOException, NoPortsAvailableException {
        this.setOriginalImage();
        this.setSplitParameters();

        BufferedImage finalImage = this.onCallingSobel();

        String filename = this.filenameFromPath(this.originalImageUrl) + FILENAME_SUFFIX;
        String extension = this.getFilenameExtension(this.originalImageUrl);
        System.out.println("Image saved in: " + this.saveImage(finalImage, filename, extension) );
    }

    private void setSplitParameters() {
        this.rowsToSplitImage = this.askForRows();
        this.colsToSplitImage = this.askForCols();
        this.redundantPixels = this.askForRedundantPixels();
    }

    private int askForRows() {
        final int DEFAULT_ROWS = 3;
        final int MIN_VALUE = 1;
        int out = DEFAULT_ROWS;

        System.out.print(String.format("Number of rows to split image [%d]: ", DEFAULT_ROWS));
        String rowsEntered = sc.nextLine();
        try {
            int value = Integer.parseInt(rowsEntered);
            if (!(value < MIN_VALUE))
                out = value;
        } catch (NumberFormatException e){}

        return out;
    }

    private int askForCols() {
        final int DEFAULT_COLS = 3;
        final int MIN_VALUE = 1;
        int out = DEFAULT_COLS;

        System.out.print(String.format("Number of cols to split image [%d]: ", DEFAULT_COLS));
        String rowsEntered = sc.nextLine();
        try {
            int value = Integer.parseInt(rowsEntered);
            if (!(value < MIN_VALUE))
                out = value;
        } catch (NumberFormatException e){}

        return out;
    }

    private int askForRedundantPixels() {
        final int DEFAULT_REDUNDANT_PIXELS = 1;
        final int MIN_VALUE = 0;
        int out = DEFAULT_REDUNDANT_PIXELS;
        System.out.print(String.format("Number of redundant pixels  [%d]: ", DEFAULT_REDUNDANT_PIXELS));
        String rowsEntered = sc.nextLine();
        try {
            int value = Integer.parseInt(rowsEntered);
            if (!(value < MIN_VALUE))
                out = value;
        } catch (NumberFormatException e){}
        return out;
    }

    private void setOriginalImage() throws IOException {
        this.originalImageUrl = askForImage();
        this.originalImage = this.imageFromUrl(this.originalImageUrl);
    }

    private BufferedImage onCallingSobel() throws RemoteException, NoPortsAvailableException {
        CommonMain.createSection("Applying Sobel Operator");
        long startTime = System.currentTimeMillis();
        BufferedImage finalImage = this.edgeDetectorClient.detectEdges(
                this.originalImage,
                this.rowsToSplitImage,
                this.colsToSplitImage,
                this.redundantPixels
        );
        long endTime = System.currentTimeMillis();
        CommonMain.createSection("Execution time: " + (endTime - startTime) + " miliseconds");

        return finalImage;
    }

    private String saveImage(BufferedImage image, String filename, String extension) throws IOException {
        File f = new File(this.imagesPath + "/" + filename + "." + extension);
        ImageIO.write(image, extension, f);
        return f.getAbsolutePath();
    }

    private String filenameFromPath(String filename) {
        return filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf("."));
    }

    private String getFilenameExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    private String askForImage() {
        String DEFAULT = "http://tutorialspoint.com/java_dip/images/digital_image_processing.jpg";
        final String SUPER_BIG_IMAGE = "http://4k.com/wp-content/uploads/2014/06/4k-image-santiago.jpg";
        //DEFAULT = "https://www.tutorialspoint.com/java_dip/images/grayscale.jpg";
        //DEFAULT = "http://www.smalljpg.com/temp/20170424223049.jpg";
        //DEFAULT = "http://www.smalljpg.com/temp/20170424224711.jpg";
        //DEFAULT = "https://s29.postimg.org/kjex7dx6f/300px-_Valve_original_1.png";
        DEFAULT = "http://4.bp.blogspot.com/_6ZIqLRChuQg/TF0-bhL6zoI/AAAAAAAAAoE/56OJXkRAFz4/s1600/lenaOriginal.png";
        //DEFAULT = SUPER_BIG_IMAGE;
        System.out.print(String.format("Enter image url [%s]: ", DEFAULT));
        String imageUrl = sc.nextLine();

        return (imageUrl.isEmpty())? DEFAULT : imageUrl;
    }

    private BufferedImage imageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        System.out.println("Reading image from url: " + imageUrl);
        return ImageIO.read(url);
    }

}

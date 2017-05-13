package Tp2.Ex04.Client;

import Common.CommonMain;
import Common.PropertiesManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class RunClient {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex04/config.properties";

    public static void main(String[] args){

        try {
            Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);

            String serverHost = properties.getProperty("SERVER_HOST");
            int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            String imagesPath = properties.getProperty("IMAGES_PATH");
            String[] servicesDNS = properties.getProperty("SERVICES_DNS").split(",");

            RunClient runClient = new RunClient(serverHost, serverPort, imagesPath, servicesDNS);
            runClient.start();
        } catch (Exception e) {
            CommonMain.display("Error: " + e.toString());
            System.exit(1);
        }
    }

    private static final String FILENAME_SUFFIX = "_sobel_filter";

    private Scanner sc = new Scanner(System.in);
    private String host;
    private int port;
    private String imagesPath;
    private EdgeDetectorClient edgeDetectorClient;

    public RunClient(String serverHost, int serverPort, String imagesPath, String[] servicesDNS) throws RemoteException, NotBoundException, ConnectException {
        newClient(serverHost, serverPort, servicesDNS);
        this.imagesPath = imagesPath;
    }

    private void newClient(String host, int port, String[] servicesDNS) throws RemoteException, NotBoundException {
        this.host = host;
        this.port = port;
        this.edgeDetectorClient = new EdgeDetectorClient(host, port, servicesDNS);
    }

    public void start() throws IOException {
        String imageUrl = askForImage();
        BufferedImage originalImage = this.imageFromUrl(imageUrl);

        BufferedImage finalImage = this.onCallingSobel(originalImage);

        String filename = this.filenameFromPath(imageUrl) + FILENAME_SUFFIX;
        String extension = this.getFilenameExtension(imageUrl);

        System.out.println("Image saved in: " + this.saveImage(finalImage, filename, extension) );
    }

    private BufferedImage onCallingSobel(BufferedImage originalImage) throws RemoteException {
        System.out.println("Image read. Starting...");
        long startTime = System.currentTimeMillis();
        BufferedImage finalImage = this.edgeDetectorClient.detectEdges(originalImage);
        long endTime = System.currentTimeMillis();
        CommonMain.createSection("Execution time: " + (endTime - startTime) + " miliseconds");

        return finalImage;
    }

    private String saveImage(BufferedImage image, String filename, String extension) throws IOException {
        File f = new File(this.imagesPath + filename + "." + extension);
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
        DEFAULT = "https://s29.postimg.org/kjex7dx6f/300px-_Valve_original_1.png";
        //DEFAULT = "http://4.bp.blogspot.com/_6ZIqLRChuQg/TF0-bhL6zoI/AAAAAAAAAoE/56OJXkRAFz4/s1600/lenaOriginal.png";
        //DEFAULT = SUPER_BIG_IMAGE;
        System.out.print("Enter image url (default image if no input): ");
        String imageUrl = sc.nextLine();

        return (imageUrl.isEmpty())? DEFAULT : imageUrl;
    }

    private BufferedImage imageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        System.out.println("Reading image from url: " + imageUrl);
        BufferedImage image = ImageIO.read(url);

        return image;
    }

}

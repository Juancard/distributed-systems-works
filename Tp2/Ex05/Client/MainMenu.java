package Tp2.Ex05.Client;

import Common.CommonMain;
import Tp2.Ex05.Client.EdgeDetectorClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class MainMenu {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5025;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 5;
    private static final String EXERCISE_TITLE = "Sobel Operator";

    private static final String IMAGES_PATH = "distributed-systems-works/Tp2/Ex05/Resources/Images/";

    private Scanner sc = new Scanner(System.in);
    private String host;
    private int port;
    private EdgeDetectorClient edgeDetectorClient;

    public static void main(String[] args) throws IOException {
        MainMenu mainMenu = null;

        try {
            mainMenu = new MainMenu(DEFAULT_HOST, DEFAULT_PORT);
        } catch (NotBoundException e) {
            CommonMain.display("Error starting RMI client: " + e.toString());
            System.exit(1);
        }

        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        mainMenu.start();
    }

    public MainMenu(String defaultHost, int defaultPort) throws RemoteException, NotBoundException, ConnectException {
        newClient(defaultHost, defaultPort);
    }

    private void newClient(String defaultHost, int defaultPort) throws RemoteException, NotBoundException {
        this.host = CommonMain.askForHost(defaultHost);
        this.port = CommonMain.askForPort(defaultPort);
        this.edgeDetectorClient = new EdgeDetectorClient(host, port);
    }

    public void start() throws IOException {
        String imageUrl = askForImage();
        BufferedImage originalImage = this.imageFromUrl(imageUrl);

        BufferedImage finalImage = this.onCallingSobel(originalImage);

        String filename = this.filenameFromPath(imageUrl) + "_edged";
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
        File f = new File(IMAGES_PATH + filename + "." + extension);
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

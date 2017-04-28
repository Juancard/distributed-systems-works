package Tp2.Ex04.Client;

import Common.CommonMain;
import Tp2.Ex04.Common.ImageChunkHandler;
import Tp2.Ex04.Common.SobelEdgeDetector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
        URL url = new URL(imageUrl);
        BufferedImage image = ImageIO.read(url);

        image = this.edgeDetectorClient.detectEdges(image);

        File f = new File(IMAGES_PATH + this.filenameFromPath(imageUrl) + "_from_rmi_single_process" + "." + this.getFilenameExtension(imageUrl));
        ImageIO.write(image, this.getFilenameExtension(imageUrl), f);

        System.out.println("Image saved in: " + f.getAbsolutePath());

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
        DEFAULT = "http://4.bp.blogspot.com/_6ZIqLRChuQg/TF0-bhL6zoI/AAAAAAAAAoE/56OJXkRAFz4/s1600/lenaOriginal.png";

        System.out.print("Enter image url: ");
        String imageUrl = sc.nextLine();

        return (imageUrl.isEmpty())? DEFAULT : imageUrl;
    }

}

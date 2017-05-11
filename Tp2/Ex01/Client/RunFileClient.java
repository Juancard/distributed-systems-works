package Tp2.Ex01.Client;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex01.Common.FileClient;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class RunFileClient {

    private static Scanner sc = new Scanner(System.in);
    private static FileClient myFileClient;

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";
    private static Properties properties;

    public static void main(String[] args) throws IOException {
        properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);
        newClient();
        handleMainOptions();
    }


    private static void newClient() {
        String host = properties.getProperty("SERVER_HOST");
        int port = Integer.parseInt(properties.getProperty("SERVER_PORT"));

        myFileClient = new FileClient(host, port);
    }

    private static void handleMainOptions() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
                CommonMain.createSection("List files");
                handleFilesAvailable();
                CommonMain.pause();
            } else if (opcion.equals("2")){
                CommonMain.createSection("Get a file");
                handleGetFile();
                CommonMain.pause();
            } else if (opcion.equals("3")){
                CommonMain.createSection("Post a file");
                handlePostFile();
                CommonMain.pause();
            } else if (opcion.equals("4")){
                CommonMain.createSection("Delete a file");
                handleDelFile();
                CommonMain.pause();
            }
        }
        myFileClient.close();
    }

    private static void handleFilesAvailable() {
        try {
            String[] filesAvailable = myFileClient.dir();
            for (String fileName : filesAvailable)
                CommonMain.display(fileName);
        } catch (Exception e) {
            CommonMain.display(e.getMessage());
        }

    }

    private static void handleGetFile() {
        System.out.print("Enter File Name: ");
        String fileName = sc.nextLine();
        try {
            String fileContent = myFileClient.get(fileName);
            CommonMain.display("File name: " + fileName);
            CommonMain.display("Content: " + fileContent);
        } catch (Exception e) {
            CommonMain.display(e.getMessage());
        }
    }

    private static void handlePostFile() {
        System.out.print("Enter File Name: ");
        String fileName = sc.nextLine();
        System.out.print("Enter File content: ");
        String fileContent = sc.nextLine();
        try {
            if (myFileClient.post(fileName, fileContent))
                CommonMain.display("File successfully created");
            else
                CommonMain.display("File could not be added. Try again later.");
        } catch (Exception e) {
            CommonMain.display(e.getMessage());
        }
    }

    private static void handleDelFile() {
        System.out.print("Enter File Name: ");
        String fileName = sc.nextLine();

        try {
            if (myFileClient.del(fileName))
                CommonMain.display("File successfully deleted");
            else
                CommonMain.display("File could not be deleted. Try again later.");
        } catch (Exception e) {
            CommonMain.display(e.getMessage());
        }
    }

    public static void showMain(){
        CommonMain.createSection("File Server - Main");
        System.out.println("1 - List files");
        System.out.println("2 - Get a file");
        System.out.println("3 - Post a file");
        System.out.println("4 - Delete a file");
        System.out.println("0 - Salir");
        System.out.println("");
        System.out.print("Ingrese opción: ");
    }



}

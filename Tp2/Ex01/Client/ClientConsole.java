package Tp2.Ex01.Client;

import Common.CommonMain;

import java.io.File;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class ClientConsole {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5021;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 1;

    private static Scanner sc = new Scanner(System.in);
    private static FileClient myFileClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "File Server");
        newClient();
        handleMainOptions();
    }


    private static void newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
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
        String[] filesAvailable = myFileClient.dir();
        for (String fileName : filesAvailable)
            CommonMain.display(fileName);
    }

    private static void handleGetFile() {
        System.out.print("Enter File Name: ");
        String fileName = sc.nextLine();
        String fileContent = myFileClient.get(fileName);

        CommonMain.display("File name: " + fileName);
        CommonMain.display("Content: " + fileContent);
    }

    private static void handlePostFile() {
        System.out.print("Enter File Name: ");
        String fileName = sc.nextLine();
        System.out.print("Enter File content: ");
        String fileContent = sc.nextLine();
        if (myFileClient.post(fileName, fileContent))
            CommonMain.display("File successfully created");
        else
            CommonMain.display("File could not be added. Try again later.");
    }

    private static void handleDelFile() {
        CommonMain.display(myFileClient.del());
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

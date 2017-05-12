package Tp2.Ex07.Client;

import Common.CommonMain;
import Common.FileException;
import Common.PropertiesManager;
import Tp2.Ex07.Common.LoginException;
import Tp2.Ex07.Common.PermissionException;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * User: juan
 * Date: 08/05/17
 * Time: 14:34
 */
public class ClientMain {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    public static void main(String[] args) throws IOException {
        ClientMain clientMain = new ClientMain();
        clientMain.start();
    }

    private Properties properties;
    private FileClient fileClient;
    private Scanner scanner;

    public ClientMain(){
        try {
            this.properties = PropertiesManager.loadProperties(ClientMain.PROPERTIES_PATH);
            this.scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        this.connectToServer();
        CommonMain.showWelcomeMessage(this.properties);
        this.showLogin();
        this.handleMainOptions();
    }

    private void connectToServer() {
        String host = this.properties.getProperty("SERVER_HOST");
        int port = Integer.parseInt(this.properties.getProperty("SERVER_PORT"));
        this.fileClient = new FileClient(host, port);
    }

    private void showLogin() {
        CommonMain.createSection("Log in");
        boolean isLogged = false;

        while (!isLogged){
            System.out.print("Username: ");
            String username = this.scanner.nextLine();
            System.out.print("Password: ");
            String password = this.scanner.nextLine();
            try {
                this.fileClient.login(username, password);
                CommonMain.display("Success: User has logged in");
                isLogged = true;
            } catch (LoginException e) {
                CommonMain.display("Error: " + e.getMessage());
            }
        }

    }

    private void handleMainOptions() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = this.scanner.nextLine();
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
                //handleDelFile();
                CommonMain.pause();
            }
        }
        this.fileClient.close();
    }

    private void handleFilesAvailable() {
        try {
            String[] filesAvailable = this.fileClient.dir();
            for (String fileName : filesAvailable)
                CommonMain.display(fileName);
        } catch (Exception e) {
            CommonMain.display("Error: " + e.getMessage());
        }

    }

    private void handleGetFile() {
        System.out.print("Enter File Name: ");
        String fileName = this.scanner.nextLine();
        try {
            String fileContent = this.fileClient.get(fileName);
            CommonMain.display("File name: " + fileName);
            CommonMain.display("Content: " + fileContent);
        } catch (PermissionException e) {
            CommonMain.display("Error with user permissions: " + e.getMessage());
        } catch (FileException e) {
            CommonMain.display("Error: " + e.getMessage());
        }
    }
    private void handlePostFile() {
        System.out.print("Enter File Name: ");
        String fileName = this.scanner.nextLine();
        System.out.print("Enter File content: ");
        String fileContent = this.scanner.nextLine();
        try {
            if (this.fileClient.post(fileName, fileContent))
                CommonMain.display("File successfully created");
            else
                CommonMain.display("File could not be added. Try again later.");
        } catch (PermissionException e) {
            CommonMain.display("Error with user permissions: " + e.getMessage());
        } catch (FileException e) {
            CommonMain.display("Error: " + e.getMessage());
        }
    }
                        /*
    private void handleDelFile() {
        System.out.print("Enter File Name: ");
        String fileName = this.scanner.nextLine();

        if (this.fileClient.del(fileName))
            CommonMain.display("File successfully deleted");
        else
            CommonMain.display("File could not be deleted. Try again later.");
    }
                        */
    public void showMain(){
        CommonMain.createSection("Main");
        System.out.println("1 - List files");
        System.out.println("2 - Get a file");
        System.out.println("3 - Post a file");
        System.out.println("4 - Delete a file");
        System.out.println("0 - Salir");
        System.out.println("");
        System.out.print("Ingrese opción: ");
    }



}

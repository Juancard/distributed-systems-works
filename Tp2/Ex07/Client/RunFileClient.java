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
public class RunFileClient {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    public static void main(String[] args){
        Properties properties = null;

        try {
            properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        } catch (IOException e) {
            CommonMain.display("Error: Could not load properties file. \nCause: " + e.getMessage());
            System.exit(1);
        }

        try {
            CommonMain.showWelcomeMessage(properties);

            String host = properties.getProperty("BALANCER_HOST");
            int port = Integer.parseInt(properties.getProperty("BALANCER_PORT"));

            RunFileClient runFileClient = new RunFileClient(host, port);
            runFileClient.start();
        } catch (Exception e) {
            String m = "Error: " + e.getMessage();
            CommonMain.display(m);
            System.exit(1);
        }
    }

    private FileClient fileClient;
    private Scanner scanner;

    public RunFileClient(String host, int port) throws IOException {
        this.fileClient = this.connectToServer(host, port);
        this.scanner = new Scanner(System.in);
    }

    private void start() {
        this.showLogin();
        this.handleMainOptions();
    }

    private FileClient connectToServer(String host, int port) throws IOException {
        try {
            return new FileClient(host, port);
        } catch (IOException e) {
            String m = "Could not connect to file server. Cause: " + e.getMessage();
            throw new IOException(m);
        }
    }

    private void showLogin() {
        CommonMain.createSection("Log in");
        boolean isLogged = false;
        boolean lostConnection = this.fileClient.isClosed();

        while (!isLogged && !lostConnection){
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
            } catch (Exception e){
                this.fileClient.close();
                CommonMain.display("Error: " + e.getMessage());
            }

            lostConnection = this.fileClient.isClosed();
        }

        if (lostConnection) this.onConnectionLost();
    }

    private void onConnectionLost(){
        CommonMain.display("Sorry: Lost connection with server. Closing app.");
        System.exit(1);
    }

    private void handleMainOptions() {
        String opcion;
        boolean salir = false;
        boolean lostConnection = this.fileClient.isClosed();

        while (!salir && !lostConnection) {
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
                handleDelFile();
                CommonMain.pause();
            }
            lostConnection = this.fileClient.isClosed();
        }
        if (lostConnection) this.onConnectionLost();
        else this.fileClient.close();
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
        } catch (Exception e) {
            CommonMain.display("Error: " + e.getMessage());
            this.fileClient.close();
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
        } catch (Exception e) {
            CommonMain.display("Error: " + e.getMessage());
            this.fileClient.close();
        }
    }

    private void handleDelFile() {
        System.out.print("Enter File Name: ");
        String fileName = this.scanner.nextLine();

        try {
            if (this.fileClient.del(fileName))
                CommonMain.display("File successfully deleted");
            else
                CommonMain.display("File could not be deleted. Try again later.");
        } catch (Exception e) {
            CommonMain.display("Error: " + e.getMessage());
        }
    }

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

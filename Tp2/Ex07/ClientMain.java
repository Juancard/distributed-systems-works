package Tp2.Ex07;

import Common.CommonMain;
import Common.PropertiesManager;

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
        Properties properties = PropertiesManager.loadProperties(ClientMain.PROPERTIES_PATH);
        ClientMain clientMain = new ClientMain(properties);
        clientMain.start();
    }

    private Properties properties;
    private FileClient fileClient;
    private User loggedUser;

    public ClientMain(Properties properties){
        this.properties = properties;
    }

    private void start() {
        this.connectToServer();
        this.showWelcomeMessage();
        this.showLogin();
    }

    private void connectToServer() {
        String host = this.properties.getProperty("HOST");
        int port = Integer.parseInt(this.properties.getProperty("PORT"));
        this.fileClient = new FileClient(host, port);
    }

    private void showLogin() {
        CommonMain.createSection("Log in");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        this.fileClient.logIn(username, password);

    }

    private void showWelcomeMessage() {
        int tpNumber = Integer.parseInt(properties.getProperty("TP_NUMBER"));
        int exerciseNumber = Integer.parseInt(properties.getProperty("EXERCISE_NUMBER"));
        String exerciseTitle = properties.getProperty("EXERCISE_TITLE");

        CommonMain.showWelcomeMessage(tpNumber, exerciseNumber, exerciseTitle);
    }


}

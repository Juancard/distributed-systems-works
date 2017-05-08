package Tp2.Ex07.Client;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex01.Client.RunFileClient;
import Tp2.Ex07.Common.User;

import javax.security.auth.login.LoginException;
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

    public ClientMain(){
        try {
            this.properties = PropertiesManager.loadProperties(ClientMain.PROPERTIES_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        this.connectToServer();
        CommonMain.showWelcomeMessage(this.properties);
        this.showLogin();
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
            Scanner scanner = new Scanner(System.in);
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            try {
                this.fileClient.login(username, password);
            } catch (LoginException e) {
                CommonMain.display(e.getMessage());
            }
        }

    }


}

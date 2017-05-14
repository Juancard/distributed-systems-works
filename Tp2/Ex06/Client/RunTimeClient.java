package Tp2.Ex06.Client;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.EOFException;
import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class RunTimeClient {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex06/config.properties";

    private TimeClient timeClient;

    public static void main(String[] args) {
        try {
            Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);

            int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            String serverHost = properties.getProperty("SERVER_HOST");

            RunTimeClient runTimeClient = new RunTimeClient(serverHost, serverPort);
            runTimeClient.start();
        } catch (IOException e) {
            String m = "Error: Could not connect to Time Server. Cause: " + e.getMessage();
            CommonMain.display(m);
            System.exit(1);
        }
    }

    public RunTimeClient(String host, int port) throws IOException {
        this.timeClient = new TimeClient(host, port);
    }


    public void start() {
        try {
            this.timeClient.startTimeListener();
        } catch (EOFException e){
            CommonMain.display("Server closed time connection. Closing app.");
        } catch(Exception e) {
            CommonMain.display("Error: " + e.getMessage());
        }
    }
}

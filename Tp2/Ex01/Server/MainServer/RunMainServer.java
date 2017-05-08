package Tp2.Ex01.Server.MainServer;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunMainServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";

    public static void main(String[] args) {

        try {
            Properties properties = PropertiesManager.loadProperties(RunMainServer.PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);

            int port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            String filesPath = properties.getProperty("SERVER_FILES_PATH");
            String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");

            MainServer mainServer = new MainServer(port, filesPath, logFilePath);
            mainServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

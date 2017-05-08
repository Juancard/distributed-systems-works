package Tp2.Ex01.Server.MainServer;

import Common.CommonMain;
import Common.PropertiesManager;
import Common.ServerInfo;

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
            // Configuration data on servers
            Properties properties = PropertiesManager.loadProperties(RunMainServer.PROPERTIES_PATH);

            CommonMain.showWelcomeMessage(properties);

            // Main server data
            int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            String filesPath = properties.getProperty("SERVER_FILES_PATH");
            String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");

            // Backup server data
            String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
            int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
            ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

            // Run server
            MainServer mainServer = new MainServer(serverPort, backupServerInfo, filesPath, logFilePath);
            mainServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

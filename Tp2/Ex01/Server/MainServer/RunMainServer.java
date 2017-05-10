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
            new RunMainServer(properties).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int serverPort;
    protected String filesPath;
    protected String logFilePath;
    protected ServerInfo backupServerInfo;


    public RunMainServer(Properties properties) throws IOException {
        CommonMain.showWelcomeMessage(properties);

        // Main server data
        this.serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        this.filesPath = properties.getProperty("SERVER_FILES_PATH");
        this.logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        this.backupServerInfo = new ServerInfo(backupHost, backupPort);

    }

    protected void run() throws IOException {
        // Run server
        MainServer mainServer = new MainServer(this.serverPort, this.backupServerInfo, this.filesPath, this.logFilePath);
        mainServer.startServer();
    }
}

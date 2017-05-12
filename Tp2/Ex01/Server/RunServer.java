package Tp2.Ex01.Server;

import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex01.Server.MainServer.MainServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 15:26
 */
public class RunServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";

    private Properties properties;
    private Thread backupServer;
    private Thread mainServer;


    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        RunServer runServer = new RunServer(properties);
        runServer.start();
    }

    public RunServer(Properties properties) throws IOException {
        this.properties = properties;
        this.prepareBackupServer();
        this.prepareMainServer();
    }

    private void prepareBackupServer() throws IOException {
        int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        String filesPath = properties.getProperty("BACKUP_FILES_PATH");
        String logFilePath = properties.getProperty("BACKUP_LOG_FILE_PATH");

        BackupServer backupServer = new BackupServer(port, filesPath, logFilePath);
        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServer() throws IOException {

        // Main server data;
        int serverPort = Integer.parseInt(this.properties.getProperty("SERVER_PORT"));
        String filesPath = properties.getProperty("SERVER_FILES_PATH");
        String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

        MainServer mainServer = new MainServer(
                serverPort,
                backupServerInfo,
                filesPath,
                logFilePath
        );
        this.mainServer = new Thread(mainServer);

    }

    private void start() {
        this.backupServer.start();
        this.mainServer.start();
    }
}

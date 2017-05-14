package Tp2.Ex01.Server;

import Common.CommonMain;
import Common.FileManager;
import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex01.Server.MainServer.MainServer;

import java.io.*;
import java.util.Properties;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 15:26
 */
public class RunFileServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";

    private Properties properties;
    private Thread backupServer;
    private Thread mainServer;


    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);
        RunFileServer runFileServer = new RunFileServer(properties);
        runFileServer.start();
    }

    public RunFileServer(Properties properties) throws IOException {
        this.properties = properties;
        this.prepareBackupServer();
        this.prepareMainServer();
    }

    private void prepareBackupServer() throws IOException {
        CommonMain.createSection("Preparing Backup Server");

        int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        String filesPathValue = properties.getProperty("BACKUP_FILES_PATH");
        File filesPath = FileManager.loadFilesPath(filesPathValue);

        BackupServer backupServer = new BackupServer(port, filesPath);

        String logFilePath = properties.getProperty("BACKUP_LOG_FILE_PATH");
        backupServer.setLogWriter(this.createLogWriter(logFilePath));
        CommonMain.display("This server will be listening on port: " + port);

        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServer() throws IOException {
        CommonMain.createSection("Preparing Main Server");

        // Main server data;
        int serverPort = Integer.parseInt(this.properties.getProperty("SERVER_PORT"));

        String filesPathValue = properties.getProperty("SERVER_FILES_PATH");
        File filesPath = FileManager.loadFilesPath(filesPathValue);

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

        MainServer mainServer = new MainServer(
                serverPort,
                backupServerInfo,
                filesPath
        );

        String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");
        mainServer.setLogWriter(this.createLogWriter(logFilePath));
        CommonMain.display("This server will be listening on port: " + serverPort);

        this.mainServer = new Thread(mainServer);

    }

    private PrintStream createLogWriter(String logFilePath) throws IOException {
        File logFile = new File(logFilePath);
        if (! (logFile.exists())){
            CommonMain.display("Log file was not found. Creating it...");
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new IOException("Could not create log file. Cause: " + e.getMessage());
            }
        }
        CommonMain.display("Logs in: " + logFile.getPath());

        return new PrintStream(
                new FileOutputStream(
                        logFile,
                        true
                )
        );
    }


    private void start() {
        CommonMain.createSection("Starting server");

        this.backupServer.start();
        this.mainServer.start();

        CommonMain.display("Server is up and running!");
    }

}

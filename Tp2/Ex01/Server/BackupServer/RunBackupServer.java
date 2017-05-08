package Tp2.Ex01.Server.BackupServer;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunBackupServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";

    public static void main(String[] args) {
        try {
            Properties properties = PropertiesManager.loadProperties(RunBackupServer.PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);

            int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
            String filesPath = properties.getProperty("BACKUP_FILES_PATH");
            String logFilePath = properties.getProperty("BACKUP_LOG_FILE_PATH");

            BackupServer backupServer = new BackupServer(port, filesPath, logFilePath);
            backupServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

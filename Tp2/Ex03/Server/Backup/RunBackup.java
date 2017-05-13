package Tp2.Ex03.Server.Backup;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex01.Server.BackupServer.BackupServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunBackup {
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex03/config.properties";

    public static void main(String[] args) {
        try {
            Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
            CommonMain.showWelcomeMessage(properties);
            int port = Integer.parseInt(properties.getProperty("BACKUP_PORT"));
            String backupAccountsPath = properties.getProperty("BACKUP_ACCOUNTS_PATH");
            String backupLogPath = properties.getProperty("BACKUP_LOG_PATH");
            BackupServer backupServer = new BackupServer(port, backupAccountsPath, backupLogPath);
            backupServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

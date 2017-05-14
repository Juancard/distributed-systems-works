package Tp2.Ex03.Server;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex03.Server.Bank.AccountsManagerWithBackup;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 15:26
 */
public class RunBankServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex03/config.properties";

    private Properties properties;
    private Thread backupServer;
    private Thread mainServer;


    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);
        RunBankServer runBankServer = new RunBankServer(properties);
        runBankServer.start();
    }

    public RunBankServer(Properties properties) throws IOException {
        this.properties = properties;
        this.prepareBackupServer();
        this.prepareMainServer();
    }

    private void prepareBackupServer() throws IOException {
        int port = Integer.parseInt(properties.getProperty("BACKUP_PORT"));
        String backupAccountsPath = properties.getProperty("BACKUP_ACCOUNTS_PATH");
        String backupLogPath = properties.getProperty("BACKUP_LOG_PATH");

        BackupServer backupServer = new BackupServer(port, backupAccountsPath);


        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServer() throws IOException {

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_PORT"));

        // Main server data
        int portToDeposit = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int portToExtract = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));
        String accountsPath = properties.getProperty("ACCOUNTS_PATH");
        AccountsManagerWithBackup accountsManagerWithBackup = new AccountsManagerWithBackup(backupHost, backupPort, accountsPath);

        Tp2.Ex02.Server.RunBankServer runBankServer = new Tp2.Ex02.Server.RunBankServer(portToDeposit, portToExtract, accountsManagerWithBackup);

        this.mainServer = new Thread(runBankServer);

    }

    private void start() {
        this.backupServer.start();
        this.mainServer.start();
    }
}

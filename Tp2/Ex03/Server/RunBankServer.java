package Tp2.Ex03.Server;

import Common.CommonMain;
import Common.FileManager;
import Common.PropertiesManager;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex03.Server.Bank.AccountsManagerWithBackup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
        CommonMain.createSection("Preparing Backup Server");

        int port = Integer.parseInt(properties.getProperty("BACKUP_PORT"));
        CommonMain.display("This server will be listening on port: " + port);

        String backupAccountsPath = properties.getProperty("BACKUP_ACCOUNTS_PATH");
        File backupAccounts = FileManager.loadFilesPath(backupAccountsPath);

        BackupServer backupServer = new BackupServer(port, backupAccounts);

        String backupLogPath = properties.getProperty("BACKUP_LOG_PATH");
        if (new File(backupLogPath).isDirectory())
            backupLogPath = new File(backupLogPath).toString() + "/backup_server.log";

        backupServer.setLogWriter(
                this.createLogWriter(backupLogPath)
        );

        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServer() throws IOException {
        CommonMain.createSection("Preparing Main Servers");

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_PORT"));

        // Main server data
        int portToDeposit = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int portToExtract = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));
        CommonMain.display("This server will be listening on ports: " + portToDeposit + " and " + portToExtract);

        String accountsPathValue = properties.getProperty("ACCOUNTS_PATH");
        File accountsPath = FileManager.loadFilesPath(accountsPathValue);

        AccountsManagerWithBackup accountsManagerWithBackup = new AccountsManagerWithBackup(backupHost, backupPort, accountsPath);

        Tp2.Ex02.Server.RunBankServer runBankServer = new Tp2.Ex02.Server.RunBankServer(portToDeposit, portToExtract, accountsManagerWithBackup);

        this.mainServer = new Thread(runBankServer);

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

    }
}

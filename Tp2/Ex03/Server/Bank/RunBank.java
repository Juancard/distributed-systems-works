package Tp2.Ex03.Server.Bank;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex02.Server.RunBankServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBank {
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex03/config.properties";

    public static void main(String[] args) throws IOException {

        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_PORT"));

        // Main server data
        int portToDeposit = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int portToExtract = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));
        String accountsPath = properties.getProperty("ACCOUNTS_PATH");
        AccountsManagerWithBackup accountsManagerWithBackup = new AccountsManagerWithBackup(backupHost, backupPort, accountsPath);

        RunBankServer runBankServer = new RunBankServer(portToDeposit, portToExtract, accountsManagerWithBackup);
        runBankServer.run();
    }
}

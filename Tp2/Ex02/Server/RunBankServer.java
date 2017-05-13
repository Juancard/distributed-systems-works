package Tp2.Ex02.Server;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBankServer implements Runnable{
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex02/config.properties";

    private AccountsManager accountsManager;
    private int portToDeposit;
    private int portToExtract;
    private BankServer bankDepositServer;
    private BankServer bankExtractServer;

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        AccountsManager accountsManager = new AccountsManager(properties.getProperty("ACCOUNTS_PATH"));
        int portToDeposit = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int portToExtract = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));

        CommonMain.showWelcomeMessage(properties);

        RunBankServer runBankServer = new RunBankServer(portToDeposit, portToExtract, accountsManager);
        runBankServer.run();
    }

    public RunBankServer(int portToDeposit, int portToExtract, AccountsManager accountsManager) throws IOException {
        this.accountsManager = accountsManager;
        this.portToDeposit = portToDeposit;
        this.portToExtract = portToExtract;
        this.setServers();
    }

    private void setServers() throws IOException {
        this.bankDepositServer = new BankServer(this.portToDeposit, this.accountsManager);
        this.bankExtractServer = new BankServer(this.portToExtract, this.accountsManager);
    }

    @Override
    public void run() {
        Thread depositThread = new Thread(bankDepositServer);
        Thread extractThread = new Thread(bankExtractServer);

        System.out.println("Opening ports:");
        System.out.println("- " + this.portToDeposit + ": port to deposit.");
        System.out.println("- " + this.portToExtract + ": port to extract.");
        System.out.println();

        depositThread.start();
        extractThread.start();
    }
}

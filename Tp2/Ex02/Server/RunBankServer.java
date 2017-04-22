package Tp2.Ex02.Server;

import Common.CommonMain;

import java.io.IOException;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBankServer implements Runnable{
    private static final String ACCOUNTS_PATH = "distributed-systems-works/Tp2/Ex02/Server/Resources/Accounts";
    private static final int DEFAULT_PORT_DEPOSIT = 5122;
    private static final int DEFAULT_PORT_EXTRACT = 5222;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String TP_TITLE = "Bank Server";

    private AccountsManager accountsManager;
    private int portToDeposit;
    private int portToExtract;
    private BankServer bankDepositServer;
    private BankServer bankExtractServer;

    public static void main(String[] args) throws IOException {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
        RunBankServer runBankServer = new RunBankServer(DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT, new AccountsManager(ACCOUNTS_PATH));
        runBankServer.run();
    }

    public RunBankServer(int defaultDepositPort, int defaultExtractPort, AccountsManager accountsManager) throws IOException {
        this.accountsManager = accountsManager;
        this.setPorts(defaultDepositPort, defaultExtractPort);
        this.setServers();
    }

    private void setServers() throws IOException {
        this.bankDepositServer = new BankServer(this.portToDeposit, this.accountsManager);
        this.bankExtractServer = new BankServer(this.portToExtract, this.accountsManager);
    }

    private void setPorts(int defaultDepositPort, int defaultExtractPort) {
        this.portToDeposit = CommonMain.askForPort("Enter deposit port", defaultDepositPort);
        this.portToExtract = CommonMain.askForPort("Enter extract port", defaultExtractPort);
    }


    @Override
    public void run() {
        (new Thread(bankDepositServer)).start();
        (new Thread(bankExtractServer)).start();
    }
}

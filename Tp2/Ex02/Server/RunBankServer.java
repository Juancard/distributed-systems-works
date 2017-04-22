package Tp2.Ex02.Server;

import Common.CommonMain;

import java.io.IOException;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBankServer {
    private static final String ACCOUNTS_PATH = "distributed-systems-works/Tp2/Ex02/Server/Resources/Accounts";
    private static final int DEFAULT_PORT_DEPOSIT = 5122;
    private static final int DEFAULT_PORT_EXTRACT = 5222;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String TP_TITLE = "Bank Server";

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
        RunBankServer runBankServer = new RunBankServer(DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT, ACCOUNTS_PATH);
    }

    public RunBankServer(int defaultDepositPort, int defaultExtractPort, String accountsPath) {
        try {
            AccountsManager accountsManager = new AccountsManager(accountsPath);
            int portToDeposit = CommonMain.askForPort("Enter deposit port", defaultDepositPort);
            int portToExtract = CommonMain.askForPort("Enter extract port", defaultExtractPort);

            BankServer bankDepositServer = new BankServer(portToDeposit, accountsManager);
            BankServer bankExtractServer = new BankServer(portToExtract, accountsManager);

            (new Thread(bankDepositServer)).start();
            (new Thread(bankExtractServer)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

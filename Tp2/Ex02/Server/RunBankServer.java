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
        try {
            AccountsManager accountsManager = new AccountsManager(ACCOUNTS_PATH);
            int portToDeposit = CommonMain.askForPort("Enter deposit port", DEFAULT_PORT_DEPOSIT);
            int portToExtract = CommonMain.askForPort("Enter extract port", DEFAULT_PORT_EXTRACT);

            BankServer bankDepositServer = new BankServer(portToDeposit, accountsManager);
            BankServer bankExtractServer = new BankServer(portToExtract, accountsManager);

            (new Thread(bankDepositServer)).start();
            (new Thread(bankExtractServer)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

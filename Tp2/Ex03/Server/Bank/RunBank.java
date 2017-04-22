package Tp2.Ex03.Server.Bank;

import Common.CommonMain;
import Tp2.Ex02.Server.RunBankServer;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBank {
    private static final String ACCOUNTS_PATH = "distributed-systems-works/Tp2/Ex03/Server/Resources/Accounts";
    private static final int DEFAULT_PORT_DEPOSIT = 5123;
    private static final int DEFAULT_PORT_EXTRACT = 5223;

    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 3;
    private static final String TP_TITLE = "Bank Server with backup";

    public static void main(String[] args) {
            CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
            RunBankServer runBankServer = new RunBankServer(DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT, ACCOUNTS_PATH);
    }
}

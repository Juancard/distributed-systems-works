package Tp2.Ex02.Server;

import Common.CommonMain;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBankServer {
    private static final int DEFAULT_PORT = 5022;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String TP_TITLE = "Bank Server";

    public static void main(String[] args) {

        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
        BankServer bankServer = new BankServer(CommonMain.askForPort(DEFAULT_PORT));

    }
}

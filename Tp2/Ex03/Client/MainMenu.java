package Tp2.Ex03.Client;

import Common.CommonMain;
import Tp2.Ex02.Client.ClientConsole;

/**
 * User: juan
 * Date: 21/04/17
 * Time: 21:31
 */
public class MainMenu {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT_DEPOSIT = 5123;
    private static final int DEFAULT_PORT_EXTRACT = 5223;

    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String EXERCISE_TITLE = "Bank Server with backup";

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        ClientConsole clientConsole = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT);
    }


}

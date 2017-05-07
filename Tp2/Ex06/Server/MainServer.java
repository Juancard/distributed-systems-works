package Tp2.Ex06.Server;

import Common.CommonMain;

import java.io.IOException;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:12
 */
public class MainServer {
    private static final int DEFAULT_PORT = 5026;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 6;
    private static final String TP_TITLE = "Time Server - Server side";

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        int updatePeriod = 6000;
        TimeServer timeServer = new TimeServer(port, updatePeriod);
        timeServer.startServer();
    }
}

package Tp2.Ex06.Server;

import Common.CommonMain;

import java.io.IOException;
import java.util.Scanner;

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
        int updatePeriod = askForUpdatePeriod();
        TimeServer timeServer = new TimeServer(port, updatePeriod);
        timeServer.startServer();
    }

    private static int askForUpdatePeriod() {
        Scanner sc = new Scanner(System.in);
        int period = -1;

        do {
            System.out.print("Enter seconds to wait until updating the time: ");
            String periodString = sc.nextLine();
            try {
                period = Integer.parseInt(periodString);
                if (period <= 0)
                    CommonMain.display("Value can not be equal or less than zero");
            } catch (NumberFormatException e){
                CommonMain.display("Not a valid value.");
            }
        } while(period <= 0);

        return period * 1000;
    }
}

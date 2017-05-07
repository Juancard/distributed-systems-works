package Tp2.Ex06.Client;

import Common.CommonMain;

import java.util.Date;
import java.util.Scanner;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 17:30
 */
public class MainMenu {
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 6;
    private static final String EXERCISE_TITLE = "Time Server";

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5026;

    private Scanner sc = new Scanner(System.in);
    private TimeClient timeClient;

    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        mainMenu.start();


    }

    public MainMenu() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        this.timeClient = new TimeClient(host, port);
    }


    public void start() {
        while (true) {
            try {
                Date currentDate = this.timeClient.getCurrentDate();
                CommonMain.display("Client: Server date is " + currentDate.toString());
            } catch (Exception e) {
                CommonMain.display(e.getMessage());
                return;
            }
        }
    }
}

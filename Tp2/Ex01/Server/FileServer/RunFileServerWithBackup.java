package Tp2.Ex01.Server.FileServer;

import Common.CommonMain;

import java.io.IOException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunFileServerWithBackup {

    private static final int DEFAULT_PORT = 5021;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 1;
    private static final String TP_TITLE = "File Server with Backup";

    public static void main(String[] args) {

        try {
            CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
            FileServerWithBackup fileServerWithBackup = new FileServerWithBackup(CommonMain.askForPort(DEFAULT_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

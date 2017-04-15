package Tp2.Ex01.Server.BackupServer;

import Common.CommonMain;

import java.io.IOException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunBackupServer {

    private static final int DEFAULT_PORT = 5121;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 1;
    private static final String TP_TITLE = "Backup Server";

    public static void main(String[] args) {
        try {
            CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
            BackupServer backupServer = new BackupServer(CommonMain.askForPort(DEFAULT_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Tp2.Ex03.Server.Backup;

import Common.CommonMain;
import Tp2.Ex01.Server.BackupServer.BackupServer;

import java.io.IOException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunBackup {

    private static final int DEFAULT_PORT = 5323;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 3;
    private static final String TP_TITLE = "Bank server";

    private static final String FILES_PATH = "distributed-systems-works/Tp2/Ex03/Server/Backup/Resources/Files/";

    public static void main(String[] args) {
        try {
            CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE + " - Running backup");
            int port = CommonMain.askForPort("Backup server port", DEFAULT_PORT);
            BackupServer backupServer = new BackupServer(port, FILES_PATH);
            backupServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

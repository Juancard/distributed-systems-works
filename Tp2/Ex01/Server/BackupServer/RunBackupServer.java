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

    private static final String FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/BackupServer/Resources/Files/";
    private static final String LOG_FILE_PATH = "distributed-systems-works/Tp2/Ex01/Server/BackupServer/Resources/Log/backup_server_log.txt";

    public static void main(String[] args) {
        try {
            CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);
            int port = CommonMain.askForPort("Backup server port", DEFAULT_PORT);
            BackupServer backupServer = new BackupServer(port, FILES_PATH, LOG_FILE_PATH);
            backupServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

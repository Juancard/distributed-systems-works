package Tp2.Ex03.Server.Bank;

import Common.CommonMain;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex02.Server.RunBankServer;

import java.io.IOException;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:05
 */
public class RunBank {
    private static final String LOCAL_ACCOUNTS_PATH = "distributed-systems-works/Tp2/Ex03/Server/Resources/Accounts";

    private static final int DEFAULT_PORT_DEPOSIT = 5123;
    private static final int DEFAULT_PORT_EXTRACT = 5223;

    private static final String DEFAULT_BACKUP_HOST = "localhost";
    private static final int DEFAULT_PORT_BACKUP = 5323;

    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 3;
    private static final String TP_TITLE = "Bank Server with backup";

    public static void main(String[] args) throws IOException {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, TP_TITLE);

        String backupHost = CommonMain.askForHost("Backup server host", DEFAULT_BACKUP_HOST);
        int backupPort = CommonMain.askForPort("Backup server port", DEFAULT_PORT_BACKUP);
        BackupAccountManager backupAccountManager = new BackupAccountManager(backupHost, backupPort, LOCAL_ACCOUNTS_PATH);

        RunBankServer runBankServer = new RunBankServer(DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT, backupAccountManager);
        runBankServer.run();
    }
}

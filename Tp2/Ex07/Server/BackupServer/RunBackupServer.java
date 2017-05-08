package Tp2.Ex07.Server.BackupServer;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex01.Server.BackupServer.BackupServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunBackupServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    public static void main(String[] args) {
        try {
            Properties properties = PropertiesManager.loadProperties(RunBackupServer.PROPERTIES_PATH);
            new Tp2.Ex01.Server.BackupServer.RunBackupServer(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

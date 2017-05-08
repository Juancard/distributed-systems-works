package Tp2.Ex07.Server.MainServer;

import Common.CommonMain;
import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Server.MainServer.MainServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunMainServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    public static void main(String[] args) {

        try {
            Properties properties = PropertiesManager.loadProperties(RunMainServer.PROPERTIES_PATH);
            new Tp2.Ex01.Server.MainServer.RunMainServer(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

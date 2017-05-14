package Tp2.Ex06.Server;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:12
 */
public class RunTimeServer {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex06/config.properties";

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);
        int port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        int updatePeriod = Integer.parseInt(properties.getProperty("TIME_UPDATE_PERIOD_MILISECONDS"));
        TimeServer timeServer = new TimeServer(port, updatePeriod);
        timeServer.startServer();
    }

}

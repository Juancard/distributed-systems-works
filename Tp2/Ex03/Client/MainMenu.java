package Tp2.Ex03.Client;

import Common.CommonMain;
import Common.PropertiesManager;
import Tp2.Ex02.Client.ClientConsole;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 21/04/17
 * Time: 21:31
 */
public class MainMenu {
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex03/config.properties";

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);

        String host = properties.getProperty("SERVER_HOST");
        int portToDeposit = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int portToExtract = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));
        ClientConsole clientConsole = new ClientConsole(host, portToDeposit, portToExtract);
    }


}

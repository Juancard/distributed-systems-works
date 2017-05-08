package Tp2.Ex07;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 08/05/17
 * Time: 14:34
 */
public class ClientMain {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(ClientMain.PROPERTIES_PATH);

        int tpNumber = Integer.parseInt(properties.getProperty("TP_NUMBER"));
        int exerciseNumber = Integer.parseInt(properties.getProperty("EXERCISE_NUMBER"));
        String exerciseTitle = properties.getProperty("EXERCISE_TITLE");

        CommonMain.showWelcomeMessage(tpNumber, exerciseNumber, exerciseTitle);
    }

}

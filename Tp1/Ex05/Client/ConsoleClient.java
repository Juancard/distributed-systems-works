package Tp1.Ex05.Client;

import Common.CommonMain;
import Tp1.Ex05.Weather;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:17
 */
public class ConsoleClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5005;
    private static final int EXERCISE_NUMBER = 3;
    private static final int TP_NUMBER = 1;

    private static Scanner sc = new Scanner(System.in);
    private static WeatherClient weatherClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "RMI Weather ServerWithSocket");
        try {
            weatherClient = newClient();
            CommonMain.createSection("Weather in ServerWithSocket");
            showWeather(weatherClient.getWeatherInServer());
        } catch (RemoteException e) {
            CommonMain.display(e.toString());
        } catch (NotBoundException e) {
            CommonMain.display(e.toString());
        }

    }

    private static WeatherClient newClient() throws RemoteException, NotBoundException {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        return new WeatherClient(host, port);
    }

    private static void showWeather(Weather w) {
        String weatherString = String.format(
                "The weather is %s, with a temperature of %.2f°",
                w.getDescription(),
                w.getTemperature()
        );
        CommonMain.display(weatherString);
    }


}

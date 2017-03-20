package tp1.ej05.Client;

import tp1.ej05.Weather;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:17
 */
public class ConsoleClient {
    private static final String DEFAULT_IP = "localhost";
    private static final int DEFAULT_PORT = 5005;
    private static Scanner sc = new Scanner(System.in);
    private static WeatherClient weatherClient;

    public static void main(String[] args) {
        showWelcomeMessage();
        try {
            weatherClient = newClient();
            createSection("Weather in Server");
            showWeather(weatherClient.getWeatherInServer());
        } catch (RemoteException e) {
            display(e.toString());
        } catch (NotBoundException e) {
            display(e.toString());
        }

    }

    private static WeatherClient newClient() throws RemoteException, NotBoundException {
        String host = askForHost();
        int port = askForPort();
        return new WeatherClient(host, port);
    }

    private static void showWeather(Weather w) {
        String weatherString = String.format(
                "The weather is %s, with a temperature of %.2f°",
                w.getDescription(),
                w.getTemperature()
        );
        display(weatherString);
    }

    private static String askForHost(){
        System.out.printf("Enter server Ip or host [%s]: ", DEFAULT_IP);
        String ip = sc.nextLine();
        if (ip.length() == 0)
            ip = DEFAULT_IP;
        return ip;
    }

    private static int askForPort(){
        System.out.printf("Enter server Port [%s]: ", DEFAULT_PORT);
        String givenPort = sc.nextLine();
        int port;
        if (givenPort.length() == 0)
            port = DEFAULT_PORT;
        else
            try {
                port = Integer.parseInt(givenPort);
            } catch(NumberFormatException e){
                System.out.println("Not a valid port number. Default port has been set.");
                port = DEFAULT_PORT;
            }

        return port;
    }

    private static void showWelcomeMessage() {
        String title = "Sistemas Distribuidos y Programación Paralela";
        String subtitle = "TP N°1 - Ej5 - RMI Weather Server";
        createSection(title + "\n" + subtitle);
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private static void createSection(String section){
        String separatorChar = "*";
        int separatorMinLength = 6;
        int longestString = -1;

        for (String s : section.split("\n"))
            if (s.length() > longestString) longestString = s.length();

        String separator = repeat(separatorMinLength + longestString, separatorChar);
        showSection(separator, section);
    }

    private static void showSection(String separator, String section) {
        display("");
        display(separator);
        display(section);
        display(separator);
        display("");
    }

    private static void display(String toDisplay){
        System.out.println(toDisplay);
    }
}

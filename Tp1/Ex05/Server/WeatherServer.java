package Tp1.Ex05.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:32
 */
public class WeatherServer {

    public static final int PORT = 5005;
    public static final String WEATHER_SERVICE = "WEATHER_SERVICE";
    private Registry rmiServer;

    public static void main(String[] args) {
        try {
            WeatherServer weatherServer = new WeatherServer();
        } catch (RemoteException e) {
            e.printStackTrace();
            //display(e.toString());
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
            //display(e.toString());
        }
    }


    public WeatherServer() throws RemoteException, AlreadyBoundException {
        this.createRmiServer();
        this.supplyWeatherService();
    }

    private void createRmiServer() throws RemoteException {
        this.rmiServer = LocateRegistry.createRegistry(WeatherServer.PORT);
        display("RMI Server listening on port " + PORT + "...");
    }

    private void supplyWeatherService() throws RemoteException, AlreadyBoundException {
        WeatherService weatherService = new WeatherService();
        this.rmiServer.rebind(WEATHER_SERVICE, weatherService);
        UnicastRemoteObject.exportObject(weatherService, WeatherServer.PORT);

        display(String.format("Weather service is up as \"%s\" on port %d", WEATHER_SERVICE, PORT));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

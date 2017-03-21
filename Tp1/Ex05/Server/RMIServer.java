package Tp1.Ex05.Server;

import Tp1.Ex05.Server.WeatherApi.*;

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
public class RMIServer {

    public static final int PORT = 5005;
    public static final String WEATHER_SERVICE = "WEATHER_SERVICE";

    private Registry registry;

    public static void main(String[] args) {
        try {
            RMIServer RMIServer = new RMIServer();
        } catch (NoApiIdException e) {
            display(e.toString());
        } catch (RemoteException e) {
            display(e.toString());
        } catch (AlreadyBoundException e) {
            display(e.toString());
        }
    }


    public RMIServer() throws RemoteException, AlreadyBoundException, NoApiIdException {
        this.createRmiServer();
        this.supplyWeatherService();
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(RMIServer.PORT);
        display("RMI Server listening on port " + PORT + "...");
    }

    private void supplyWeatherService() throws RemoteException, AlreadyBoundException, NoApiIdException {
        WeatherService weatherService = new WeatherService();
        this.registry.rebind(WEATHER_SERVICE, weatherService);
        UnicastRemoteObject.exportObject(weatherService, RMIServer.PORT);

        display(String.format("Weather service is up as \"%s\" on port %d", WEATHER_SERVICE, PORT));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

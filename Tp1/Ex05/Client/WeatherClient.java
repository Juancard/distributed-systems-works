package Tp1.Ex05.Client;

import Tp1.Ex05.Common.IWeatherService;
import Tp1.Ex05.Common.Weather;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:00
 */
public class WeatherClient {

    private IWeatherService weatherService;

    public WeatherClient(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.weatherService = (IWeatherService) registry.lookup("VECTOR_SERVICE");
    }

    public Weather getWeatherInServer() throws RemoteException {
        return this.weatherService.getWeatherInServer();
    }
}

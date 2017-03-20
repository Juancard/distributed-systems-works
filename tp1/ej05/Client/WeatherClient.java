package tp1.ej05.Client;

import tp1.ej05.IWeatherService;
import tp1.ej05.Weather;

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
        this.weatherService = (IWeatherService) registry.lookup("WEATHER_SERVICE");
    }

    public Weather getWeatherInServer() throws RemoteException {
        return this.weatherService.getWeatherInServer();
    }
}

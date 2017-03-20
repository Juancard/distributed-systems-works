package Tp1.Ex05;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:23
 */
public interface IWeatherService extends Remote {
    public Weather getWeatherInServer() throws RemoteException;
}

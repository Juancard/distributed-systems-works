package Tp1.Ex06.Client;

import Tp1.Ex06.Common.IVectorService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:00
 */
public class VectorClient {

    private IVectorService iVectorService;

    public VectorClient(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.iVectorService = (IVectorService) registry.lookup(IVectorService.DNS_NAME);
    }

    public int[] add(int[] vector1, int[] vector2) throws RemoteException {
        return this.iVectorService.add(vector1, vector2);
    }
    public int[] substract(int[] vector1, int[] vector2) throws RemoteException {
        return this.iVectorService.substract(vector1, vector2);
    }
}

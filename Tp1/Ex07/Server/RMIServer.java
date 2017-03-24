package Tp1.Ex07.Server;

import Tp1.Ex07.Common.IMessageService;

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

    public static final int SERVER_PORT = 5007;
    public static final int MESSAGE_SERVICE_PORT = 6007;

    private Registry registry;

    public static void main(String[] args) {
        try {
            RMIServer RMIServer = new RMIServer();
        } catch (RemoteException e) {
            display(e.toString());
        } catch (AlreadyBoundException e) {
            display(e.toString());
        }
    }


    public RMIServer() throws RemoteException, AlreadyBoundException {
        this.createRmiServer();
        this.supplyWeatherService();
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(RMIServer.SERVER_PORT);
        display("RMI Server listening on port " + SERVER_PORT + "...");
    }

    private void supplyWeatherService() throws RemoteException, AlreadyBoundException {
        MessageService vectorService = new MessageService();
        IMessageService iMessageService = (IMessageService) UnicastRemoteObject.exportObject(vectorService, RMIServer.MESSAGE_SERVICE_PORT);
        this.registry.rebind(IMessageService.DNS_NAME, iMessageService);

        display(String.format("Message service is up as \"%s\" on port %d", IMessageService.DNS_NAME, MESSAGE_SERVICE_PORT));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

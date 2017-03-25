package Tp1.Ex08.Server;

import Tp1.Ex08.Common.ITaskService;

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

    public static final int SERVER_PORT = 5008;
    public static final int TASK_SERVICE_PORT = 6008;

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
        this.supplyTaskService();
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(RMIServer.SERVER_PORT);
        display("RMI Server listening on port " + SERVER_PORT + "...");
    }

    private void supplyTaskService() throws RemoteException, AlreadyBoundException {
        TaskService vectorService = new TaskService();
        ITaskService iTaskService = (ITaskService) UnicastRemoteObject.exportObject(vectorService, RMIServer.TASK_SERVICE_PORT);
        this.registry.rebind(ITaskService.DNS_NAME, iTaskService);

        display(String.format("Message service is up as \"%s\" on port %d", ITaskService.DNS_NAME, RMIServer.TASK_SERVICE_PORT));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

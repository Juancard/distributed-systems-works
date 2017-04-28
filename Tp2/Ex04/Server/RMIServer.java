package Tp2.Ex04.Server;

import Tp2.Ex04.Common.IEdgeDetectorService;

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

    public static final int SERVER_PORT = 5024;
    public static final int EDGE_DETECTOR_SERVICE_PORT = 5124;

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
        this.supplyEdgeDetectorService();
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(RMIServer.SERVER_PORT);
        display("RMI Server listening on port " + SERVER_PORT + "...");
    }

    private void supplyEdgeDetectorService() throws RemoteException, AlreadyBoundException {
        EdgeDetectorService edgeDetectorService = new EdgeDetectorService();
        IEdgeDetectorService iEdgeDetectorService = (IEdgeDetectorService) UnicastRemoteObject.exportObject(edgeDetectorService, RMIServer.EDGE_DETECTOR_SERVICE_PORT);
        this.registry.rebind(IEdgeDetectorService.DNS_NAME, iEdgeDetectorService);

        display(String.format("Edge detector service is up as \"%s\" on port %d", IEdgeDetectorService.DNS_NAME, RMIServer.EDGE_DETECTOR_SERVICE_PORT));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

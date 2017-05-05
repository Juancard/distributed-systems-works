package Tp2.Ex05.Server;

import Tp2.Ex05.Common.IEdgeDetectorService;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:32
 */
public class RMIServer implements Runnable{

    private Registry registry;
    private EdgeDetectorService edgeDetectorService;
    private int port;

    public RMIServer(int port){
        this.port = port;
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(port);
        display("RMI Server listening on port " + port + "...");
    }

    private void supplyEdgeDetectorService() throws RemoteException, AlreadyBoundException {
        String dns = IEdgeDetectorService.DNS_NAME;
        this.edgeDetectorService = new EdgeDetectorService();
        edgeDetectorService.setId(dns + "_" + this.port);
        IEdgeDetectorService iEdgeDetectorService = (IEdgeDetectorService) UnicastRemoteObject.exportObject(
                edgeDetectorService, this.port
        );
        this.registry.rebind(dns, iEdgeDetectorService);
        display(String.format("Edge detector service is up as \"%s\" on port %d", dns, this.port));
    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }

    @Override
    public void run() {
        try {
            this.createRmiServer();
            this.supplyEdgeDetectorService();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // access the service
        try {
            registry.unbind(IEdgeDetectorService.DNS_NAME);

            // get rid of the service object
            UnicastRemoteObject.unexportObject(this.edgeDetectorService, true);

            // get rid of the rmi registry
            UnicastRemoteObject.unexportObject(this.registry, true);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}

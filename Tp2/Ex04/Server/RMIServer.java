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
    public static final int EDGE_DETECTOR_SERVICE_PORT_1 = 5124;
    public static final int EDGE_DETECTOR_SERVICE_PORT_2 = 5224;
    public static final int EDGE_DETECTOR_SERVICE_PORT_3 = 5324;
    public static final int EDGE_DETECTOR_SERVICE_PORT_4 = 5424;
    public static final int EDGE_DETECTOR_OPEN_PORTS = 4;


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
        int currentPort;
        String currentDns;
        for (int i=1; i <= EDGE_DETECTOR_OPEN_PORTS; i++){
            EdgeDetectorService edgeDetectorService = new EdgeDetectorService();
            try {
                currentPort = RMIServer.class.getField("EDGE_DETECTOR_SERVICE_PORT_" + i).getInt(null);
                currentDns = (String) IEdgeDetectorService.class.getField("DNS_NAME_" + i).get(null);
                edgeDetectorService.setId(currentDns);
                IEdgeDetectorService iEdgeDetectorService = (IEdgeDetectorService) UnicastRemoteObject.exportObject(edgeDetectorService, currentPort);
                this.registry.rebind(currentDns, iEdgeDetectorService);
                display(String.format("Edge detector service is up as \"%s\" on port %d", currentDns, currentPort));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
         }

    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

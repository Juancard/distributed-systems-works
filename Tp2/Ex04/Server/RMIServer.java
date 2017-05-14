package Tp2.Ex04.Server;

import Common.PropertiesManager;
import Tp2.Ex04.Common.IEdgeDetectorService;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:32
 */
public class RMIServer {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex04/config.properties";


    public static final int SERVER_PORT = 5024;
    public static final int EDGE_DETECTOR_SERVICE_PORT_1 = 5224;
    public static final int EDGE_DETECTOR_SERVICE_PORT_2 = 5324;
    public static final int EDGE_DETECTOR_SERVICE_PORT_3 = 5424;
    public static final int EDGE_DETECTOR_SERVICE_PORT_4 = 5524;
    public static final int EDGE_DETECTOR_OPEN_PORTS = 4;


    private Registry registry;
    private int serverPort;
    private int[] servicesPorts;
    private String[] servicesDNS;

    public static void main(String[] args) {
        try {
            Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);

            int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            String[] servicesDNSGiven = properties.getProperty("SERVICES_DNS").split(",");
            String[] servicesPortsGiven = properties.getProperty("SERVICES_PORTS").split(",");

            int[] servicesPorts = new int[servicesDNSGiven.length];
            for (int i=0; i<servicesPorts.length; i++)
                servicesPorts[i] = Integer.parseInt(servicesPortsGiven[i]);

            RMIServer RMIServer = new RMIServer(serverPort, servicesPorts, servicesDNSGiven);
        } catch (RemoteException e) {
            display(e.toString());
        } catch (AlreadyBoundException e) {
            display(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public RMIServer(int serverPort, int[] servicesPorts, String[] servicesDNS) throws RemoteException, AlreadyBoundException {
        this.serverPort = serverPort;
        this.servicesPorts = servicesPorts;
        this.servicesDNS = servicesDNS;
        this.createRmiServer();
        this.supplyEdgeDetectorService();
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(this.serverPort);
        display("RMI Server listening on port " + this.serverPort + "...");
    }

    private void supplyEdgeDetectorService() throws RemoteException, AlreadyBoundException {

        int currentPort; String currentDns; EdgeDetectorService edgeDetectorService;
        for (int i=0; i<this.servicesPorts.length; i++){
            edgeDetectorService = new EdgeDetectorService();
            currentPort = this.servicesPorts[i];
            currentDns = this.servicesDNS[i];
            edgeDetectorService.setId(currentDns);
            IEdgeDetectorService iEdgeDetectorService = (IEdgeDetectorService) UnicastRemoteObject.exportObject(edgeDetectorService, currentPort);
            this.registry.rebind(currentDns, iEdgeDetectorService);
            display(String.format("Edge detector service is up as \"%s\" on port %d", currentDns, currentPort));
        }

    }


    private static void display(String toDisplay) {
        System.out.println(toDisplay);
    }
}

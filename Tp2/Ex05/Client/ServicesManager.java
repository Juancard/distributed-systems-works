package Tp2.Ex05.Client;

import Tp2.Ex05.Common.IEdgeDetectorService;
import Tp2.Ex05.Common.NoPortsAvailableException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

/**
 * User: juan
 * Date: 06/05/17
 * Time: 11:10
 */
public class ServicesManager {

    private static final int TOTAL_PORTS_AVAILABLE = 4;

    private HashMap<Integer, Registry> registries;

    // maps a port to the amount of connections established to that port
    // so to gain load balancing between ports
    private HashMap<Integer, Integer> portsAvailable;

    public ServicesManager(String host, int port) {
        this.registries = new HashMap<Integer, Registry>();
        this.portsAvailable = new HashMap<Integer, Integer>();

        this.connectToRegistries(host, port);
    }

    public IEdgeDetectorService getService() throws NoPortsAvailableException {
        HashMap<Integer, Integer> portsCurrentlyAvailable = this.portsAvailable;

        boolean connected = false;
        boolean noPortsAvailable = portsCurrentlyAvailable.isEmpty();
        int portToConnectTo = -1;
        while (!connected && !noPortsAvailable){
            portToConnectTo = this.getMinService(portsCurrentlyAvailable);
            if (this.portIsAvailable(portToConnectTo))
                connected = true;
            else
                portsCurrentlyAvailable.remove(portToConnectTo);
            noPortsAvailable = portsCurrentlyAvailable.isEmpty();
        }                                
        if (noPortsAvailable)
            throw new NoPortsAvailableException();

        IEdgeDetectorService service = null;
        try {
            service = this.getService(portToConnectTo);
        } catch (Exception e) {e.printStackTrace();}

        return service;
    }

    private boolean portIsAvailable(int port) {
        try {
            IEdgeDetectorService service = this.getService(port);
            return true;
        } catch (RemoteException e) {
            System.out.println("Service is down in port " + port);
            return false;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void connectToRegistries(String host, int port) {
        int currentPort;
        for (int i=0; i < TOTAL_PORTS_AVAILABLE; i++){
            currentPort = port + i;
            portsAvailable.put(currentPort, 0); // 0 means: no connections established in this port.
            System.out.println("Connecting to port: " + currentPort);
            try {
                Registry r = LocateRegistry.getRegistry(host, currentPort);
                this.registries.put(currentPort, r);
            } catch (RemoteException e) {
                System.out.println("No registry available in port: " + currentPort);
                e.printStackTrace();
            }
        }
    }

    private int getMinService(HashMap<Integer, Integer> services){
        int minPort = -1;
        int minValue = 99999999;
        for (int port : services.keySet()){
            int value = services.get(port);
            if (value < minValue){
                minValue = value;
                minPort = port;
            }
        }
        return minPort;
    }

    private IEdgeDetectorService getService(int port) throws RemoteException, NotBoundException {
        String dns = IEdgeDetectorService.DNS_NAME;
        Registry r = this.registries.get(port);
        IEdgeDetectorService service = (IEdgeDetectorService) r.lookup(dns);
        this.addConnectionToPort(port);
        return service;
    }

    private void addConnectionToPort(int port){
        int amountOfConnections = this.portsAvailable.get(port);
        this.portsAvailable.put(port, amountOfConnections + 1);
    }


}

package Tp2.Ex05.Client;

import Common.ServerInfo;
import Tp2.Ex05.Common.IEdgeDetectorService;
import Tp2.Ex05.Common.NoPortsAvailableException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: juan
 * Date: 06/05/17
 * Time: 11:10
 */
public class RemotePortsManager {

    // maps a server to the amount of connections established to that server
    // so to gain load balancing between servers
    private HashMap<ServerInfo, Integer> remotePorts;
    private HashMap<ServerInfo, Registry> registries;

    public RemotePortsManager(ArrayList<ServerInfo> remotePorts) throws NoPortsAvailableException {
        this.registries = new HashMap<ServerInfo, Registry>();
        this.remotePorts = new HashMap<ServerInfo, Integer>();

        this.loadRemotePorts(remotePorts);
        this.connectToRegistries();
    }

    private void loadRemotePorts(ArrayList<ServerInfo> remotePorts) throws NoPortsAvailableException {
        for (ServerInfo remotePort : remotePorts){
            this.remotePorts.put(remotePort, 0); //0 means no connection established (to perform load balancing)
        }
    }

    private void connectToRegistries() {
        for (ServerInfo serverInfo : this.remotePorts.keySet()){
            try {
                Registry r = LocateRegistry.getRegistry(serverInfo.getHost(), serverInfo.getPort());
                this.registries.put(serverInfo, r);
            } catch (RemoteException e) {
                System.out.println("Error connecting to " + serverInfo + ": " + e.getMessage());
            }
        }
    }

    public IEdgeDetectorService getService() throws NoPortsAvailableException {
        HashMap<ServerInfo, Integer> remotePortsAvailable = this.remotePorts;

        boolean connected = false;
        boolean noRemotePortsAvailable = remotePortsAvailable.isEmpty();
        ServerInfo remotePortToConnectTo = null;
        while (!connected && !noRemotePortsAvailable){
            remotePortToConnectTo = this.getMinService(remotePortsAvailable);

            if (this.remotePortIsAvailable(remotePortToConnectTo))
                connected = true;
            else
                remotePortsAvailable.remove(remotePortToConnectTo);

            noRemotePortsAvailable = remotePortsAvailable.isEmpty();
        }                                
        if (noRemotePortsAvailable)
            throw new NoPortsAvailableException("Could not connect to any of the available ports.");

        IEdgeDetectorService service = null;
        try {
            service = this.getService(remotePortToConnectTo);
        } catch (Exception e) {e.printStackTrace();}

        return service;
    }

    private boolean remotePortIsAvailable(ServerInfo remotePort) {
        try {
            IEdgeDetectorService service = this.getService(remotePort);
            return true;
        } catch (RemoteException e) {
            System.out.println("Remote port '" + remotePort + "' is down.");
            return false;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ServerInfo getMinService(HashMap<ServerInfo, Integer> remotePorts){
        ServerInfo minRemotePort = null;
        int minValue = 99999999;
        for (ServerInfo s : remotePorts.keySet()){
            int value = remotePorts.get(s);
            if (value < minValue){
                minValue = value;
                minRemotePort = s;
            }
        }
        return minRemotePort;
    }

    private IEdgeDetectorService getService(ServerInfo remotePort) throws RemoteException, NotBoundException {
        String dns = IEdgeDetectorService.DNS_NAME;
        Registry r = this.registries.get(remotePort);
        IEdgeDetectorService service = (IEdgeDetectorService) r.lookup(dns);
        this.addConnectionToPort(remotePort);
        return service;
    }

    private void addConnectionToPort(ServerInfo remotePort){
        int amountOfConnections = this.remotePorts.get(remotePort);
        this.remotePorts.put(remotePort, amountOfConnections + 1);
    }


}

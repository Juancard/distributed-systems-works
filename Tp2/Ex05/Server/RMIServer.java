package Tp2.Ex05.Server;

import Tp2.Ex01.Server.Common.LogManager;
import Tp2.Ex05.Common.IEdgeDetectorService;

import java.io.*;
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
    private LogManager logManager;

    public RMIServer(int port){
        this.port = port;
        this.logManager = new LogManager(System.out);
    }

    private void createRmiServer() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(port);
        log("RMI Server listening on port " + port + "...");
    }

    private void supplyEdgeDetectorService() throws RemoteException, AlreadyBoundException {
        String dns = IEdgeDetectorService.DNS_NAME;
        this.edgeDetectorService = new EdgeDetectorService();
        edgeDetectorService.setId(dns + "_" + this.port);
        IEdgeDetectorService iEdgeDetectorService = (IEdgeDetectorService) UnicastRemoteObject.exportObject(
                edgeDetectorService, this.port
        );
        this.registry.rebind(dns, iEdgeDetectorService);
        log(String.format("Edge detector service is up as \"%s\" on port %d", dns, this.port));
    }


    private void log(String toDisplay) {
        this.logManager.log(toDisplay);
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

    public void setLogManager(String logFilePath) throws FileNotFoundException {
        File f = new File(logFilePath);
        if (f.isDirectory()) {
            f = new File(logFilePath + "server_" + this.port + ".log");
            if (!(f.exists()))
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        this.logManager.setLogPrinter(new PrintStream(new FileOutputStream(f, true)));
        System.out.println("Logs in: " + f.getPath());
    }
}

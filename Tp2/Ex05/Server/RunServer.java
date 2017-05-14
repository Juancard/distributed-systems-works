package Tp2.Ex05.Server;

import Common.CommonMain;
import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex05.Common.RemotePortsLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * User: juan
 * Date: 05/05/17
 * Time: 14:42
 */
public class RunServer {
    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex05/Resources/Config/config.properties";

    private ArrayList<ServerInfo> remotePorts;
    private final String logsPath;
    protected HashMap<Integer, Thread> threads;
    protected HashMap<Integer, RMIServer> servers;

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);

        if (PropertiesManager.propIsFileExists(properties, "REMOTE_PORTS_FILE"))
            throw new IOException("Remote Ports file does not exists");
        if (PropertiesManager.propIsPath(properties, "SERVER_LOG_PATH"))
            throw new IOException("Server Log Path is not a valid directory");

        String remotePortsPath = properties.getProperty("REMOTE_PORTS_FILE");
        String logsPath = properties.getProperty("SERVER_LOG_PATH");

        RunServer runServer = new RunServer(remotePortsPath, logsPath);
        if (runServer.start()) {
            ServicesKiller servicesKiller = new ServicesKiller(runServer.threads, runServer.servers);
            servicesKiller.run();
        }
    }

    public RunServer(String remotePortsPath, String logsPath) throws IOException {
        this.logsPath = logsPath;
        this.loadRemotePortsFromPath(remotePortsPath);
        this.setUpRemotePorts();
    }

    private void loadRemotePortsFromPath(String remotePortsPath) throws IOException {
        try {
            this.remotePorts = RemotePortsLoader.remotePortsFrom(remotePortsPath);
        } catch (FileNotFoundException e) {
            String m = "No file with remote ports found in: '" + remotePortsPath + "'";
            throw new FileNotFoundException(m);
        }
    }

    private void setUpRemotePorts(){
        threads = new HashMap<Integer, Thread>();
        servers = new HashMap<Integer, RMIServer>();

        CommonMain.createSection("Setting up Remote Ports");

        boolean isLocalhost;
        String host; int port; Thread thread; RMIServer server;
        for (ServerInfo remotePort : remotePorts){
            System.out.println(remotePort);
            host = remotePort.getHost();
            port = remotePort.getPort();
            isLocalhost = host.equals("localhost") || host.equals("127.0.0.1");
            if ( !isLocalhost )
                System.out.println(
                        "FAIL - Server with host " + host + " can not be started from localhost."
                );
            else {
                server = new RMIServer(port);
                try {
                    server.setLogManager(this.logsPath);
                } catch (FileNotFoundException e) {
                    System.out.println("WARN - could not set logs path to server. Cause: " + e.getMessage());
                    System.out.println("\t By default, logs will be displayed in this console.");
                }
                thread = new Thread(server);
                threads.put(port, thread);
                servers.put(port, server);
            }
            System.out.println("");
        }
    }

    private boolean start() {
        Thread t;
        for (int port : this.threads.keySet()){
            t = this.threads.get(port);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}

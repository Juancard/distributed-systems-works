package Tp2.Ex05.Server;

import Common.CommonMain;
import Common.ServerInfo;
import Tp2.Ex05.Common.RemotePortsLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * User: juan
 * Date: 05/05/17
 * Time: 14:42
 */
public class MainServer {

    private static final String EXERCISE_TITLE = "Sobel server";
    private static final String PORTS_PATH = "distributed-systems-works/Tp2/Ex05/Common/remote_ports.txt";

    private static Scanner sc = new Scanner(System.in);
    private static HashMap<Integer, Thread> threads;
    private static HashMap<Integer, RMIServer> servers;

    public static void main(String[] args) {
        if (startServers()) {
            handleMainOptions();
        }
    }

    private static boolean startServers() {
        ArrayList<ServerInfo> remotePorts;
        try {
            remotePorts = RemotePortsLoader.remotePortsFrom(PORTS_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("No file with remote ports found in: '" + PORTS_PATH + "'");
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        threads = new HashMap<Integer, Thread>();
        servers = new HashMap<Integer, RMIServer>();
        boolean isLocalhost;
        String host; int port; Thread thread; RMIServer server;

        for (ServerInfo remotePort : remotePorts){
            host = remotePort.getHost();
            port = remotePort.getPort();
            isLocalhost = host.equals("localhost") || host.equals("127.0.0.1");

            if ( !isLocalhost )
                System.out.println("WARN - Server with host " + host + " can not be started from localhost");
            else {
                server = new RMIServer(port);
                thread = new Thread(server);

                threads.put(port, thread);
                servers.put(port, server);

                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return true;
    }

    private static void showMain() {
        CommonMain.createSection(EXERCISE_TITLE + " - Main");
        System.out.println("1 - Kill service");
        System.out.println("0 - Exit");
        System.out.println("");
        System.out.print("Enter option: ");
    }

    private static void handleMainOptions() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                stopThreads();
                salir = true;
            } else if (opcion.equals("1")){
                CommonMain.createSection("Kill service");
                handleKillService();
                CommonMain.pause();
            }
        }
    }

    private static void stopThreads() {
        for (int port : threads.keySet())
            killService(port);
        threads = null;
        servers = null;
    }

    private static void showPortsAvailable() {
        CommonMain.display("Ports available: ");
        for (int port : threads.keySet()){
            CommonMain.display("- " + port);
        }
    }

    private static void handleKillService() {
        showPortsAvailable();
        System.out.print("Enter service port: ");
        int port = -1;
        try{
            port = new Integer(sc.nextLine());
        } catch (NumberFormatException e){};

        if (!(threads.containsKey(port)))
            CommonMain.display("Given port does not have a service running.");
        else {
            killService(port);
            threads.remove(port);
        }

    }

    private static void killService(int port){
        synchronized (threads){
            Thread t = threads.get(port);
            t.stop();
            RMIServer server  = servers.get(port);
            server.stop();
        }
    }
}

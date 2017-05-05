package Tp2.Ex05.Server;

import Common.CommonMain;
import Tp2.Ex05.Common.IEdgeDetectorService;

import java.util.HashMap;
import java.util.Scanner;

/**
 * User: juan
 * Date: 05/05/17
 * Time: 14:42
 */
public class MainServer {

    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 5;
    private static final String EXERCISE_TITLE = "Sobel server";

    public static final int DISTRIBUTED_PORTS = 4;
    public static final int STARTING_PORT = 5025;

    private static Scanner sc = new Scanner(System.in);
    private static HashMap<Integer, Thread> threads;
    private static HashMap<Integer, RMIServer> servers;

    public static void main(String[] args) {
        startServers();
        handleMainOptions();
        System.exit(1);
    }

    private static void startServers() {
        threads = new HashMap<Integer, Thread>();
        servers = new HashMap<Integer, RMIServer>();
        Thread thread;
        int port;
        RMIServer server;

        for (int i=0; i < DISTRIBUTED_PORTS; i++){
            port = STARTING_PORT + i;
            server = new RMIServer(port);
            thread = new Thread(server);

            threads.put(port, thread);
            servers.put(port, server);
            thread.start();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

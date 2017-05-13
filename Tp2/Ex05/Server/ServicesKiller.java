package Tp2.Ex05.Server;

import Common.CommonMain;

import java.util.HashMap;
import java.util.Scanner;

/**
 * User: juan
 * Date: 13/05/17
 * Time: 13:14
 */
public class ServicesKiller implements Runnable{
    private Scanner scanner;
    private HashMap<Integer, Thread> threads;
    private HashMap<Integer, RMIServer> servers;

    public ServicesKiller(HashMap<Integer, Thread> threads, HashMap<Integer, RMIServer> servers){
        this.scanner = new Scanner(System.in);
        this.threads = threads;
        this.servers = servers;
    }

    @Override
    public void run() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            this.showMain();
            opcion = scanner.nextLine();
            if (opcion.equals("0")) {
                this.stopThreads();
                salir = true;
            } else if (opcion.equals("1")){
                CommonMain.createSection("Kill service");
                this.handleKillService();
                CommonMain.pause();
            }
        }
    }

    private void showMain() {
        CommonMain.createSection("Services killer - Main");
        System.out.println("1 - Kill service");
        System.out.println("0 - Exit");
        System.out.println("");
        System.out.print("Enter option: ");
    }

    private void stopThreads() {
        for (int port : this.threads.keySet())
            killService(port);
        this.threads = null;
        this.servers = null;
    }

    private void showPortsAvailable() {
        CommonMain.display("Ports available: ");
        for (int port : this.threads.keySet()){
            CommonMain.display("- " + port);
        }
    }

    private void handleKillService() {
        showPortsAvailable();
        System.out.print("Enter service port: ");
        int port = -1;
        try{
            port = new Integer(scanner.nextLine());
        } catch (NumberFormatException e){};

        if (!(this.threads.containsKey(port)))
            CommonMain.display("Given port does not have a service running.");
        else {
            killService(port);
            this.threads.remove(port);
        }

    }

    private void killService(int port){
        synchronized (this.threads){
            Thread t = this.threads.get(port);
            t.stop();
            RMIServer server  = this.servers.get(port);
            server.stop();
        }
    }
}

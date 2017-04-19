package Common.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:14
 */
public class MyCustomServer {

    private int port;
    private ServerSocket serverSocket;
    private Map<Thread, Runnable> threadsPool;


    public MyCustomServer(int port) {
        this.prepareServer(port);
        this.startServer();
    }

    private void prepareServer(int port) {
        this.port = port;
        this.threadsPool = new HashMap<Thread, Runnable>();
    }

    private void startServer() {
        this.instantiateServer();
        this.handleConnections();
    }

    private void instantiateServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            String toPrint = String.format("Listening on port: %d...", this.port);
            this.out(toPrint);
        } catch (IOException e) {
            this.out("Error in creating new server socket");
            this.closeServer();
        }
    }

    private void handleConnections() {
        Socket clientSocket;
        while (!this.serverSocket.isClosed()){
            try {
                clientSocket = this.serverSocket.accept();
                this.newConnection(clientSocket);
            } catch (IOException e) {
                this.out("IOException: Error in establishing connection with client");
            }
        }
    }

    private void newConnection(Socket clientSocket) {
        Runnable runnable = this.newRunnable(clientSocket);
        Thread t = this.newThread(runnable);
        this.threadsPool.put(t, runnable);
        t.start();
        String toPrint = "New connection with client: " + clientSocket.getRemoteSocketAddress();
        this.out(toPrint);
    }

    private Thread newThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        return thread;
    }

    protected Runnable newRunnable(Socket clientSocket){
        return new Runnable() {
            @Override
            public void run() {
                // Does nothing //must be overriden
            }
        };
    }

    private void closeServer() {
        this.threadsPool.clear();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void out(String toPrint){
        System.out.println(toPrint);
    }
}

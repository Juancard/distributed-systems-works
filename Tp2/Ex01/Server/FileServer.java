package Tp2.Ex01.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class FileServer {
    private int port;
    private ServerSocket serverSocket;
    private List<ServerThread> threadsPool;

    public static void main(String[] args) {
        int port = 5021;
        FileServer fileServer = new FileServer(port);
    }


    public FileServer(int port) {
        this.prepareServer(port);
        this.startServer();
    }

    private void prepareServer(int port) {
        this.port = port;
        this.threadsPool = new ArrayList<ServerThread>();
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
        ServerThread serverThread = new ServerThread(clientSocket);
        this.threadsPool.add(serverThread);
        this.newThread(serverThread).start();
        String toPrint = "New connection with client: " + clientSocket.getRemoteSocketAddress();
        this.out(toPrint);
    }

    public Thread newThread(ServerThread serverThread){
        Thread thread = new Thread(serverThread);
        return thread;
    }

    private void closeServer() {
        Iterator i = this.threadsPool.iterator();
        while (i.hasNext()){
            ((ServerThread) i.next()).close();
        }
        this.threadsPool.clear();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void out(String toPrint){
        System.out.println(toPrint);
    }
}
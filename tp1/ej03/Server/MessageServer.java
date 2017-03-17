package tp1.ej03.Server;

import java.io.IOException;
import java.util.Iterator;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class MessageServer {

    private int port;
    private ServerSocket serverSocket;
    private List<ServerThread> threadsPool;
    private MessagesHandler messagesHandler;

    public static void main(String[] args) {
        int port = 5003;
        MessageServer messageServer = new MessageServer(port);
    }

    public MessageServer(int port) {
        this.prepareServer(port);
        this.startServer();
    }

    private void prepareServer(int port) {
        this.port = port;
        this.threadsPool = new ArrayList<ServerThread>();
        this.messagesHandler = new MessagesHandler();
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
        ServerThread serverThread = new ServerThread(clientSocket, this.messagesHandler);
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

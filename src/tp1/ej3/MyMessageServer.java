package tp1.ej3;

import java.io.IOException;
import java.util.HashMap;
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
public class MyMessageServer {

    private int port;
    private HashMap<String, List<Message>> messages;
    private ServerSocket serverSocket;
    private List<MyServerThread> connectionsPool;

    public static void main(String[] args) {
        int port = 5003;
        MyMessageServer myMessageServer = new MyMessageServer(port);
    }

    public MyMessageServer(int port) {
        this.prepareServer(port);
        this.startServer();
    }

    private void prepareServer(int port) {
        this.port = port;
        this.messages = new HashMap<String, List<Message>>();
        this.connectionsPool = new ArrayList<MyServerThread>();
    }

    private void startServer() {
        this.instantiateServer();
        this.handleConnections();
    }

    private void instantiateServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            String toPrint = String.format("Listening on port: %d...\n", this.port);
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
        MyServerThread myServerThread = new MyServerThread(clientSocket, this.messages);
        this.connectionsPool.add(myServerThread);
        this.newThread(myServerThread).start();
        String toPrint = "New connection with client: " + clientSocket.getRemoteSocketAddress();
        this.out(toPrint);
    }

    public Thread newThread(MyServerThread myServerThread){
        Thread thread = new Thread(myServerThread);
        return thread;
    }

    private void closeServer() {
        Iterator i = this.connectionsPool.iterator();
        while (i.hasNext()){
            ((MyServerThread) i.next()).close();
        }
        this.connectionsPool.clear();
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

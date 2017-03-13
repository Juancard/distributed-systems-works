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
public class MyServer {

    private int port;
    private HashMap<String, List<Message>> messages;

    public static void main(String[] args) {
        int port = 5003;
        MyServer myServer = new MyServer(port);
    }

    public MyServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            messages = new HashMap<String, List<Message>>();
            System.out.printf("Listening on port: %d...\n", port);
            while (true){
                Socket clientSocket = serverSocket.accept();
                this.newServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Thread newServerThread(Socket clientSocket){
        MyServerThread myServerThread = new MyServerThread(clientSocket, this.messages);
        Thread thread = new Thread(myServerThread);
        myServerThread.setThreadId(thread.getId());
        System.out.println("New connection with client: " + clientSocket.getRemoteSocketAddress());

        return thread;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

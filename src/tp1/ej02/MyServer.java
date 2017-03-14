package tp1.ej02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class MyServer {

    private int port;

    public static void main(String[] args) {
        int port = 5001;
        MyServer myServer = new MyServer(port);
    }

    public MyServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
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
        MyServerThread myServerThread = new MyServerThread(clientSocket);
        Thread thread = new Thread(myServerThread);
        myServerThread.setThreadId(thread.getId());

        return thread;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

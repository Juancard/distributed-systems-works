package tp1.ej3;

import java.io.IOException;
import java.util.Iterator;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class MyServer {

    private int port;
    private ArrayList<Message> messages;

    public static void main(String[] args) {
        int port = 5003;
        MyServer myServer = new MyServer(port);
    }

    public MyServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            messages = new ArrayList<Message>();
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

        return thread;
    }

    public ArrayList<Message> getMessages(String givenSource){
        ArrayList<Message> out = new ArrayList<Message>();
        Iterator i = this.messages.iterator();
        while (i.hasNext()){
            Message thisMessage = (Message) i.next();
            if (thisMessage.getSource().equals(givenSource))
                out.add(thisMessage);
        }
        return out;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

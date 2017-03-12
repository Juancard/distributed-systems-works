package tp1.ej3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class MyClient {

    private String host;
    private int port;
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5003;
        MyClient myClient = new MyClient(host, port);

        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                Object serverMessage = myClient.readMessage();
                if (serverMessage == null) break;
                System.out.println(serverMessage);
                String toSend = sc.nextLine();
                myClient.sendMessage(toSend);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public MyClient(String host, int port){
        this.host = host;
        this.port = port;
        try {
            System.out.printf("Connecting to server %s:%d\n", host, port);

            this.clientSocket = new Socket(host, port);
            this.objectOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.clientSocket.getInputStream());

            System.out.println("Client succesfully connected to: " + this.clientSocket.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object toSend){
        try {
            objectOutputStream.writeObject(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readMessage() throws IOException, ClassNotFoundException{
        return objectInputStream.readObject();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}

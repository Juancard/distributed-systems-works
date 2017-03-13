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

    public MyClient(String host, int port){
        this.host = host;
        this.port = port;
        try {

            this.clientSocket = new Socket(host, port);
            this.objectOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.clientSocket.getInputStream());

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

    public void close() {
        try {
            if (!this.clientSocket.isClosed())
                this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readMessage(){
        try {
            return objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("Error in reading messages. Closing.");
            this.close();
            return null;
        }
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

package Tp1.Ex03.Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:45
 */
public class SocketClient {

    private String host;
    private int port;
    private Socket clientSocket;
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;

    public SocketClient(String host, int port) {
        try {
            this.host = host;
            this.port = port;
            this.clientSocket = new Socket(host, port);
            this.socketOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.socketInput = new ObjectInputStream(this.clientSocket.getInputStream());
        } catch (UnknownHostException e){
        	System.out.println("UnknownHostException: Not a valid Ip and Port combination.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToSocket(Object toSend){
        try {
            socketOutput.writeObject(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Object readFromSocket(){
        try {
            return socketInput.readObject();
        } catch (Exception e) {
            System.out.println("Error in reading object from socket. Closing.");
            this.close();
            return null;
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

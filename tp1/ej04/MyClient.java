package tp1.ej04;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:45
 */
public class MyClient {

    private String host;
    private int port;
    private Socket clientSocket;
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;

    public MyClient(String host, int port){
        try {
            this.host = host;
            this.port = port;
            this.clientSocket = new Socket(host, port);
            this.socketOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.socketInput = new ObjectInputStream(this.clientSocket.getInputStream());
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

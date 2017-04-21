package Common.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 18:18
 */
public class SocketConnection {

    private Socket clientSocket;
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;


    public SocketConnection(Socket clientSocket) {
        this.startConnection(clientSocket);
    }

    public SocketConnection(String host, int port){
        try {
            Socket clientSocket = new Socket(host, port);
            this.startConnection(clientSocket);
        } catch (UnknownHostException e){
            System.out.println("UnknownHostException: Not a valid Ip and Port combination.");
        } catch (IOException e) {
            this.out("Error in instantiating new server thread: " + e.getMessage() + ".");
            this.close();
        }
    }

    private void startConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.socketOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            this.socketInput = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            this.out("Error in instantiating new server thread");
            this.close();
        }
    }

    public Object read() throws IOException, ClassNotFoundException {
        Object read = this.socketInput.readObject();
        return read;
    }

    public void send(Object toSend) {
        try {
            socketOutput.writeObject(toSend);
        } catch (IOException e) {
            out("IOException: Error in sending object to socket");
            this.close();
        }
    }

    public String getIdentity(){
        return this.clientSocket.getRemoteSocketAddress().toString();
    }

    public boolean isClosed(){
        return this.clientSocket.isClosed();
    }

    public void close () {
        this.closeInput();
        this.closeOutput();
        this.closeSocket();
    }

    private void closeInput () {
        try {
            this.socketInput.close();
        } catch (Exception e) {}
    }

    private void closeOutput () {
        try {
            this.socketOutput.close();
        } catch (Exception e) {}
    }

    private void closeSocket () {
        try {
            this.clientSocket.close ();
        } catch (Exception e) {}
    }

    public void out(String message){
        System.out.println(message);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

}

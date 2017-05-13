package Common.Socket;

import java.io.EOFException;
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

    public SocketConnection(String host, int port) throws IOException {
        try {
            Socket clientSocket = new Socket(host, port);
            this.startConnection(clientSocket);
        } catch (UnknownHostException e){
            this.close();
            throw new UnknownHostException("Not a valid Ip and Port combination.");
        } catch (IOException e) {
            this.close();
            throw new IOException(e.getMessage() + ".");
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

    public Object read() throws ClassNotFoundException, IOException {
        try {
            Object read = this.socketInput.readObject();
            return read;
        } catch(EOFException e){
            this.close();
            throw new EOFException("Connection lost.");
        } catch (IOException e) {
            String m = "Error in reading from socket. Cause: " + e.getMessage();
            this.close();
            throw new IOException(m);
        } catch (ClassNotFoundException e) {
            String m = "Error in reading from socket. Cause: " + e.getMessage();
            this.close();
            throw new ClassNotFoundException(m);
        }
    }

    public void send(Object toSend) {
        try {
            socketOutput.writeObject(toSend);
        } catch (IOException e) {
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

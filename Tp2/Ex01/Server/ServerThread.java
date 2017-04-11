package Tp2.Ex01.Server;

import Tp2.Ex01.Common.FileProtocol;
import Tp2.Ex01.Common.TextFile;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class ServerThread implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;

    private FileProtocol fileProtocol;
    private FileManager fileManager;

    public ServerThread(Socket clientSocket, FileManager fileManager) {
        try {
            this.clientSocket = clientSocket;
            this.fileProtocol = new FileProtocol();
            this.socketInput = new ObjectInputStream(clientSocket.getInputStream());
            this.socketOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            this.fileManager = fileManager;
        } catch (IOException e) {
            this.out("Error in instantiating new server thread");
            this.close();
        }
    }

    @Override
    public void run() {
        try {

            boolean socketClosed = this.clientSocket.isClosed();
            while (!socketClosed){
                Object objectFromClient = readFromSocket();
                if (objectFromClient == null) break;
                this.handleClientInput(objectFromClient);

                socketClosed = this.clientSocket.isClosed();
            }
            this.close();

        } catch (SocketException e) {
            System.out.println("Connection lost with client: " + this.clientSocket.getRemoteSocketAddress());
            this.close ();
        } catch (EOFException e) {
            System.out.println("A client has disconnected: " + this.clientSocket.getRemoteSocketAddress());
            this.close ();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleClientInput(Object objectFromClient) throws Exception {
        Object objectToClient = null;
        int protocolState = this.fileProtocol.getState();

        if (protocolState == FileProtocol.READY) {
            objectToClient = this.onClientRequest(objectFromClient.toString());
        }

        if (objectToClient != null)
            this.sendToSocket(objectToClient);
    }

    private Object onClientRequest(String request) throws Exception {
        Object out = new Object();

        if (request.equals(FileProtocol.POST)) {
            TextFile textFile = (TextFile) this.readFromSocket();
            out = fileManager.post(textFile);
        } else if (request.equals(FileProtocol.DEL)){
            String fileName = this.readFromSocket().toString();
            out = fileManager.del(fileName);
        } else if (request.equals(FileProtocol.GET)){
            String fileName = this.readFromSocket().toString();
            out = fileManager.get(fileName);
        } else if (request.equals(FileProtocol.DIR)){
            out = fileManager.dir();
        }

        return out;
    }

    public Object readFromSocket() throws IOException, ClassNotFoundException {
        Object read = this.socketInput.readObject();
        out("Read from Client: " + read);
        return read;
    }

    public void sendToSocket(Object toSend) {
        try {
            out("Sending to Client: " + toSend);
            socketOutput.writeObject(toSend);
        } catch (IOException e) {
            out("IOException: Error in sending object to socket");
            this.close();
        }
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

    private void out(String toPrint){
        System.out.println(toPrint);
    }

}

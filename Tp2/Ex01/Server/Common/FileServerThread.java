package Tp2.Ex01.Server.Common;

import Common.FileManager;
import Tp2.Ex01.Common.FileProtocol;
import Tp2.Ex01.Common.SocketConnection;
import Common.TextFile;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 23:01
 */
public class FileServerThread implements Runnable {

    protected SocketConnection clientConnection;
    protected FileProtocol fileProtocol;
    protected FileManager fileManager;
    protected LogManager logManager;

    public FileServerThread(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        this.clientConnection = clientConnection;
        this.fileProtocol = new FileProtocol();
        this.fileManager = fileManager;
        this.logManager = logManager;
    }

    @Override
    public void run() {
        try {

            boolean clientClosed = this.clientConnection.isClosed();
            while (!clientClosed){
                Object objectFromClient = this.clientConnection.read();
                if (objectFromClient == null) break;
                this.handleClientInput(objectFromClient);

                clientClosed = this.clientConnection.isClosed();
            }

        } catch (SocketException e) {
            this.logManager.log(clientConnection.getIdentity(), "Connection lost with client");
        } catch (EOFException e) {
            this.logManager.log(clientConnection.getIdentity(), "Client disconnected");
        } catch (IOException e) {
            this.logManager.log(clientConnection.getIdentity(), e.getMessage());
        } catch (Exception e) {
            this.logManager.log(clientConnection.getIdentity(), e.getMessage());
        } finally {
            this.close();
        }
    }

    private void handleClientInput(Object objectFromClient) throws Exception {
        Object objectToClient = null;
        int protocolState = this.fileProtocol.getState();

        if (protocolState == FileProtocol.READY) {
            objectToClient = this.onClientRequest(objectFromClient.toString());
        }

        if (objectToClient != null)
            this.clientConnection.send(objectToClient);
    }

    private Object onClientRequest(String request) throws Exception {
        Object out = new Object();

        this.logManager.log(this.clientConnection.getIdentity(), request);
        if (request.equals(FileProtocol.POST)) {
            out = this.post();
        } else if (request.equals(FileProtocol.DEL)){
            out = this.del();
        } else if (request.equals(FileProtocol.GET)){
            out = this.get();
        } else if (request.equals(FileProtocol.DIR)){
            out = this.dir();
        }

        return out;
    }

    protected Object dir() {
        return fileManager.dir();
    }

    protected Object get() throws IOException, ClassNotFoundException {
        String fileName = this.clientConnection.read().toString();
        return fileManager.get(fileName);
    }

    protected boolean del() throws IOException, ClassNotFoundException {
        String fileName = this.clientConnection.read().toString();
        return fileManager.del(fileName);
    }

    protected boolean post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.clientConnection.read();
        return fileManager.post(textFile);
    }

    public void close(){
        this.clientConnection.close();
    }
}

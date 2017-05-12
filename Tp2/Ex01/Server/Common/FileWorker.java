package Tp2.Ex01.Server.Common;

import Common.FileException;
import Common.FileManager;
import Common.Socket.MyCustomWorker;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileProtocol;
import Common.TextFile;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 23:01
 */
public class FileWorker extends MyCustomWorker {

    protected FileProtocol fileProtocol;
    protected FileManager fileManager;
    protected LogManager logManager;

    public FileWorker(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection);
        this.fileProtocol = new FileProtocol();
        this.fileManager = fileManager;
        this.logManager = logManager;
    }

    // This is what gets called from parent class on RUN.
    protected void handleClientInput(Object clientInput){
        Object objectToClient = null;
        try {
            objectToClient = this.onClientRequest(clientInput.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (objectToClient != null)
            this.sendToClient(objectToClient);
    }

    private Object onClientRequest(String request) throws Exception {
        Object out = new Object();

        this.display("Request - " + request);
        if (request.equals(FileProtocol.POST)) {
            out = this.post();
        } else if (request.equals(FileProtocol.DEL)){
            out = this.del();
        } else if (request.equals(FileProtocol.GET)){
            out = this.get();
        } else if (request.equals(FileProtocol.DIR)){
            out = this.dir();
        }
        this.display("Response - " + out);

        return out;
    }

    protected Object dir() {
        return fileManager.dir();
    }

    protected Object get() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        return this.get(fileName);
    }

    protected Object get(String fileName) {
        Object out;
        try {
            out =  fileManager.get(fileName);
        } catch (FileException e) {
            out = e;
        } catch (IOException e) {
            out = e;
        }
        return out;
    }

    protected Object del() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        return this.del(fileName);
    }

    protected Object del(String fileName) {
        try {
            return fileManager.del(fileName);
        } catch (FileException e) {
            return e;
        }
    }


    protected Object post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.readFromClient();
        try {
            return fileManager.post(textFile);
        } catch (FileException e) {
            return e;
        }
    }

    public void display(String message){
        this.logManager.log(this.clientIdentity(), message);
    }

    public void close(){
        if (! this.clientConnection.isClosed())
            this.clientConnection.close();
    }
}

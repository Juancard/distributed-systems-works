package Tp2.Ex07.Server.MainServer;

import Tp2.Ex07.Common.FileProtocol;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;
import Tp2.Ex01.Server.Common.LogManager;
import Tp2.Ex07.Common.User;

import java.io.IOException;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends Tp2.Ex01.Server.MainServer.MainServerConnection{

    public MainServerConnection(SocketConnection clientConnection, FileClient backupConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, backupConnection, fileManager, logManager);
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

        this.display(request);
        if (request.equals(FileProtocol.POST)) {
            out = this.post();
        } else if (request.equals(FileProtocol.DEL)){
            out = this.del();
        } else if (request.equals(FileProtocol.GET)){
            out = this.get();
        } else if (request.equals(FileProtocol.DIR)){
            out = this.dir();
        } else if (request.equals(FileProtocol.LOGIN)){
            out = this.login();
        }

        return out;
    }

    public boolean login() {
        User userLogged = null;
        try {
            userLogged = (User) this.readFromClient();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}

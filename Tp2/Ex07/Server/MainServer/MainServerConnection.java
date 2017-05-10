package Tp2.Ex07.Server.MainServer;

import Tp2.Ex07.Common.FileProtocol;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;
import Tp2.Ex01.Server.Common.LogManager;
import Tp2.Ex07.Common.LoginException;
import Tp2.Ex07.Common.User;
import Tp2.Ex07.Server.MainServer.Database.DatabaseManager;
import Tp2.Ex07.Server.MainServer.Database.UserHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends Tp2.Ex01.Server.MainServer.MainServerConnection{

    private DatabaseManager databaseManager;

    public MainServerConnection(SocketConnection clientConnection, FileClient backupConnection, String databaseUrl, FileManager fileManager, LogManager logManager) {
        super(clientConnection, backupConnection, fileManager, logManager);
        this.databaseManager = new DatabaseManager(databaseUrl);
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

    public Object login() throws SQLException, IOException, ClassNotFoundException {
        User givenUser = (User) this.readFromClient();
        String username = givenUser.getUsername();
        String pass = givenUser.getPassword();

        UserHandler userHandler = new UserHandler(this.databaseManager.getConnection());

        // username exists?
        if (!(userHandler.userExists(username)))
            return new LoginException("User '" + username + "' does not exists.");

        // password is correct?
        if (!(userHandler.isValidUserAndPass(username, pass)))
            return new LoginException("Wrong password for user '" + username + "'.");

        return true;
    }

    public void close(){
        super.close();
        try {
            this.databaseManager.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

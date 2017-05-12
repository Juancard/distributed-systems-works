package Tp2.Ex07.Server.MainServer;

import Common.TextFile;
import Tp2.Ex07.Common.FileProtocol;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;
import Tp2.Ex01.Server.Common.LogManager;
import Tp2.Ex07.Common.LoginException;
import Tp2.Ex07.Common.PermissionException;
import Tp2.Ex07.Common.User;
import Tp2.Ex07.Server.MainServer.Database.DatabaseManager;
import Tp2.Ex07.Server.MainServer.Database.PermissionHandler;
import Tp2.Ex07.Server.MainServer.Database.UserHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends Tp2.Ex01.Server.MainServer.MainServerConnection{

    private DatabaseManager databaseManager;
    private User userLogged;

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
            out = this.onPost();
        } else if (request.equals(FileProtocol.DEL)){
            out = this.del();
        } else if (request.equals(FileProtocol.GET)){
            out = this.onGet();
        } else if (request.equals(FileProtocol.DIR)){
            out = this.onDir();
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

        this.userLogged = givenUser;

        return true;
    }

    private Object onDir() throws SQLException {
        String username = this.userLogged.getUsername();
        PermissionHandler permissionHandler = new PermissionHandler(this.databaseManager.getConnection());

        // User has get permissions?
        boolean canDir = permissionHandler.hasAccessPermissionTo(username, PermissionHandler.PERMISSION_DIR);
        if (!(canDir))
            return new PermissionException(
                    "User " +
                            "'" + userLogged.getUsername() + "'" + " " +
                            "is not allowed to get list of available files"
            );

        final ArrayList<String> allFiles = new ArrayList<String>(Arrays.asList(this.fileManager.dir()));
        ArrayList<String> userFiles = permissionHandler.getFilesWithPermission(username, PermissionHandler.PERMISSION_DIR);

        ArrayList<String> out = new ArrayList<String>();
        for (String userFile : userFiles)
            if (allFiles.contains(userFile))
                out.add(userFile);

        return out.toArray(new String[out.size()]);
    }

    private Object onGet() throws IOException, ClassNotFoundException, SQLException {
        String filename = this.readFromClient().toString();
        String username = this.userLogged.getUsername();
        PermissionHandler permissionHandler = new PermissionHandler(this.databaseManager.getConnection());

        // User has get permissions?
        boolean canGet = permissionHandler.hasAccessPermissionTo(username, PermissionHandler.PERMISSION_GET);
        if (!(canGet))
            return new PermissionException(
                    "User " +
                            "'" + userLogged.getUsername() + "'" + " " +
                            "is not allowed to get files"
            );

        boolean canGetFile = permissionHandler.hasResourcePermission(username, PermissionHandler.PERMISSION_GET, filename);
        if (!canGetFile)
            return new PermissionException("No sufficient permissions to get this file");

        return super.get(filename);
    }

    protected Object onPost() throws IOException, ClassNotFoundException, SQLException {
        TextFile textFile = (TextFile) this.readFromClient();
        String username = this.userLogged.getUsername();
        PermissionHandler permissionHandler = new PermissionHandler(this.databaseManager.getConnection());

        // User has post permissions?
        boolean canPost = permissionHandler.hasAccessPermissionTo(username, PermissionHandler.PERMISSION_POST);
        if (!(canPost))
            return new PermissionException(
                "User " +
                "'" + userLogged.getUsername() + "'" + " " +
                "is not allowed to post files"
            );

        // Check if filename exists
        boolean fileExists = this.fileManager.exists(textFile.getName());

        // if file exists, user has to have post permission.
        if (fileExists){
            boolean canPostFile = permissionHandler.hasResourcePermission(username, PermissionHandler.PERMISSION_POST, textFile.getName());
            if (!(canPostFile))
                return new PermissionException("User is not allowed to post file. Cause: filename already exists");
        }

        // post file in directory
        Object postResult = super.post(textFile);
        if (postResult instanceof Exception)
            return postResult;

        boolean hasPostInDirectory = (Boolean) postResult;
        // if post is ok, save permission data in db
        if (hasPostInDirectory) {
            boolean hasInsert = permissionHandler.allPermissionsToResource(username, textFile.getName());
            return hasInsert;
        } else return false;
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

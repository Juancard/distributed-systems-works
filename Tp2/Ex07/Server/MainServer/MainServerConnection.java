package Tp2.Ex07.Server.MainServer;

import Common.FileException;
import Common.TextFile;
import Tp2.Ex07.Common.FileProtocol;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;
import Common.LogManager;
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
            this.close();
        }
        if (objectToClient != null)
            this.sendToClient(objectToClient);
    }

    private Object onClientRequest(String request) throws Exception {
        Object out = new Object();

        this.display("Request - " + request);
        if (request.equals(FileProtocol.POST)) {
            out = this.onPost();
        } else if (request.equals(FileProtocol.DEL)){
            out = this.onDel();
        } else if (request.equals(FileProtocol.GET)){
            out = this.onGet();
        } else if (request.equals(FileProtocol.DIR)){
            out = this.onDir();
        } else if (request.equals(FileProtocol.LOGIN)){
            out = this.login();
        }
        this.display("Response - " + out.toString());

        return out;
    }

    public Object login() throws SQLException, IOException, ClassNotFoundException {
        User givenUser = (User) this.readFromClient();
        this.display("User trying to log: " + givenUser);

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

        this.display("User logged successfully: " + this.userLogged);
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
        this.display("User " + username + " - GET " + filename);
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
        this.display("User " + username + " - POST " + textFile);

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

    protected Object onDel() throws IOException, ClassNotFoundException, SQLException {
        String filename =  this.readFromClient().toString();
        String username = this.userLogged.getUsername();
        this.display("User " + username + " - DEL " + filename);

        PermissionHandler permissionHandler = new PermissionHandler(this.databaseManager.getConnection());

        // User has del permissions?
        boolean canDel = permissionHandler.hasAccessPermissionTo(username, PermissionHandler.PERMISSION_DEL);
        if (!(canDel))
            return new PermissionException(
                    "User " +
                            "'" + userLogged.getUsername() + "'" + " " +
                            "is not allowed to delete files"
            );

        // Check if filename exists
        boolean fileExists = this.fileManager.exists(filename);
        if (!(fileExists))
            return new FileException("File does not exists");

        // if file exists, user has to have del permission.
        boolean canDelFile = permissionHandler.hasResourcePermission(username, PermissionHandler.PERMISSION_DEL, filename);
        if (!(canDelFile))
            return new PermissionException("No sufficient permissions to delete file");

        // file exists and user has del permission, then:
        // delete file in directory
        Object delResult = super.del(filename);
        if (delResult instanceof Exception)
            return delResult;

        boolean hasPostInDirectory = (Boolean) delResult;
        // if post is ok, update permission data in db
        if (hasPostInDirectory) {
            boolean hasDelete = permissionHandler.deleteFile(filename);
            return hasDelete;
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

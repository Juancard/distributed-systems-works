package Tp2.Ex07.Server.MainServer.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: juan
 * Date: 11/05/17
 * Time: 17:21
 */
public class UserPermissionHandler {
    public static final String PERMISSION_POST = "dir";

    private Connection dbConnection;

    public UserPermissionHandler(Connection dbConnection){
        this.dbConnection = dbConnection;
    }


    public boolean hasAccessPermissionTo(String permissionPost) throws SQLException {
        Statement st = this.dbConnection.createStatement();
        String query = "SELECT * FROM "
    }
}

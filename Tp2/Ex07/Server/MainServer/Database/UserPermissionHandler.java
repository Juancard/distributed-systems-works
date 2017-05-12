package Tp2.Ex07.Server.MainServer.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 11/05/17
 * Time: 17:21
 */
public class UserPermissionHandler {
    public static final String TBL_PERMISSION = "PERMISSION";
    public static final String ATTRIB_PERMISSION_NAME = "permission_name";
    public static final String ATTRIB_PERMISSION_ID = "permission_id";

    public static final String TBL_ACCESS_PERMISSION = "ACCESS_PERMISSION";
    public static final String TBL_RESOURCE_PERMISSION = "RESOURCE_PERMISSION";
    public static final String ATTRIB_FILENAME = "filename";

    public static final String PERMISSION_POST = "post";
    public static final String PERMISSION_GET = "get";
    public static final String PERMISSION_DEL = "del";
    public static final String PERMISSION_DIR = "dir";

    private Connection dbConnection;

    public UserPermissionHandler(Connection dbConnection){
        this.dbConnection = dbConnection;
    }

    public ArrayList<String> getAccessPermissions(String username) throws SQLException {
        Statement st = this.dbConnection.createStatement();

        String query = "SELECT "+ ATTRIB_PERMISSION_NAME + " from " + TBL_PERMISSION + " p " +
                "  join (" +
                "         select " + ATTRIB_PERMISSION_ID + " from " + TBL_ACCESS_PERMISSION + " uap " +
                "           join (" +
                "                  select " + UserHandler.ATTRIB_ID + " from " + UserHandler.TBL_NAME +
                "                             where " + UserHandler.ATTRIB_USERNAME + "='" + username +"'" +
                "                ) u" +
                "             on uap."+ UserHandler.ATTRIB_ID + "=u." + UserHandler.ATTRIB_ID +
                "       ) up on p." + ATTRIB_PERMISSION_ID + "=up." + ATTRIB_PERMISSION_ID +
                ";";
        System.out.println("Query: " + query);

        ResultSet resultSet = st.executeQuery(query);
        ArrayList<String> permissions = new ArrayList<String>();
        while (resultSet.next()) {
            permissions.add(resultSet.getString(ATTRIB_PERMISSION_NAME));
        }
        return permissions;
    }

    public boolean hasAccessPermissionTo(String username, String permission) throws SQLException {
        ArrayList<String> permissions = this.getAccessPermissions(username);
        return permissions.contains(permission);
    }

    public ArrayList<String> getResourcePermissions(String username, String filename) throws SQLException {
        Statement st = this.dbConnection.createStatement();

        String query = "SELECT " + ATTRIB_PERMISSION_NAME +
                " FROM " + TBL_RESOURCE_PERMISSION + " rp " +
                " JOIN " + UserHandler.TBL_NAME + " u ON rp. " + UserHandler.ATTRIB_ID + "=u." + UserHandler.ATTRIB_ID +
                " JOIN " + TBL_PERMISSION + " p on rp." + ATTRIB_PERMISSION_ID + "=p." + ATTRIB_PERMISSION_ID +
                " WHERE " +
                UserHandler.ATTRIB_USERNAME + "='" + username + "'" +
                " AND " +
                ATTRIB_FILENAME + "='" + filename + "'" +
                ";";
        System.out.println("Query: " + query);

        ResultSet resultSet = st.executeQuery(query);
        ArrayList<String> permissions = new ArrayList<String>();
        while (resultSet.next()) {
            permissions.add(resultSet.getString(ATTRIB_PERMISSION_NAME));
        }
        return permissions;
    }

    public boolean hasPermissionOverFile(String username, String permission, String filename) throws SQLException {
        ArrayList<String> permissions = this.getResourcePermissions(username, filename);
        return permissions.contains(permission);
    }

    public boolean newUserPermissionFile(String username, String permission, String filename) {
        return true;
    }
}

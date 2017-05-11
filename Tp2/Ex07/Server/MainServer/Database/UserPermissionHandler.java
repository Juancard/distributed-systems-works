package Tp2.Ex07.Server.MainServer.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: juan
 * Date: 11/05/17
 * Time: 17:21
 */
public class UserPermissionHandler {
    public static final String TBL_PERMISSION = "PERMISSION";
    public static final String ATTRIB_PERMISSION_NAME = "permission_name";
    public static final String ATTRIB_PERMISSION_ID = "permission_id";

    public static final String TBL_USER_PERMISSION = "USER_ACCESS_PERMISSION";

    public static final String PERMISSION_POST = "post";
    public static final String PERMISSION_GET = "get";
    public static final String PERMISSION_DEL = "del";
    public static final String PERMISSION_DIR = "dir";

    private Connection dbConnection;

    public UserPermissionHandler(Connection dbConnection){
        this.dbConnection = dbConnection;
    }


    public boolean hasAccessPermissionTo(String username, String permission) throws SQLException {
        Statement st = this.dbConnection.createStatement();

        String query = "SELECT "+ ATTRIB_PERMISSION_NAME + " from " + TBL_PERMISSION + " p " +
                "  join (" +
                "         select " + ATTRIB_PERMISSION_ID + " from " + TBL_USER_PERMISSION + " uap " +
                "           join (" +
                "                  select " + UserHandler.ATTRIB_ID + " from " + UserHandler.TBL_NAME +
                "                             where " + UserHandler.ATTRIB_USERNAME + "='" + username +"'" +
                "                ) u" +
                "             on uap."+ UserHandler.ATTRIB_ID + "=u." + UserHandler.ATTRIB_ID +
                "       ) up on p." + ATTRIB_PERMISSION_ID + "=up." + ATTRIB_PERMISSION_ID +
                ";";
        System.out.println("Query: " + query);

        ResultSet resultSet = st.executeQuery(query);
        boolean hasPermission = false;
        while (resultSet.next() && !hasPermission) {
            String currentPermission = resultSet.getString(ATTRIB_PERMISSION_NAME);
            hasPermission = currentPermission.equals(permission);
        }

        return hasPermission;
    }
}

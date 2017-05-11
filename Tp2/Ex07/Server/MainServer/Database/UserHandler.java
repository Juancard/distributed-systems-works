package Tp2.Ex07.Server.MainServer.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: juan
 * Date: 10/05/17
 * Time: 19:37
 */
public class UserHandler {
    public static final String TBL_NAME = "USER";
    public static final String ATTRIB_ID = "user_id";
    public static final String ATTRIB_USERNAME = "username";
    public static final String ATTRIB_PASS = "pass";

    private Connection dbConnection;

    public UserHandler(Connection dbConnection){
        this.dbConnection = dbConnection;
    }

    public boolean userExists(String username) throws SQLException {
        Statement st = this.dbConnection.createStatement();
        String query = "SELECT * FROM " + TBL_NAME +
                        " WHERE " + ATTRIB_USERNAME + "='" + username + "';";
        System.out.println("Query: " + query);
        ResultSet resultSet = st.executeQuery(query);

        boolean exists = resultSet.next();

        resultSet.close();st.close();
        return exists;
    }

    public boolean isValidUserAndPass(String username, String pass) throws SQLException {
        Statement st = this.dbConnection.createStatement();
        String query = "SELECT * FROM " +
                TBL_NAME +
                " WHERE " + ATTRIB_USERNAME + "='" + username + "'" +
                " AND " +
                ATTRIB_PASS + " ='" + pass + "'" +
                ";";
        System.out.println("Query: " + query);
        ResultSet resultSet = st.executeQuery(query);
        boolean exists = resultSet.next();
        st.close();resultSet.close();

        return exists;
    }
}

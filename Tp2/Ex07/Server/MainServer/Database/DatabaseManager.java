package Tp2.Ex07.Server.MainServer.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: juan
 * Date: 10/05/17
 * Time: 16:23
 */
public class DatabaseManager {

    private String databaseUrl;
    private Connection connection;

    public DatabaseManager (String databaseUrl){
        this.databaseUrl = databaseUrl;
    }

    public boolean create() throws SQLException {
        Connection conn = DriverManager.getConnection(databaseUrl);
        return conn != null;
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed())
            connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public static void testConnection(String databaseUrl) throws SQLException {
        Connection c = DriverManager.getConnection(databaseUrl);
        c.close();
    }

}

package Tp2.Ex07.Server.MainServer;

import Common.ServerInfo;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class MainServer extends Tp2.Ex01.Server.MainServer.MainServer {

    private String databaseUrl;

    public MainServer(int port, ServerInfo backupServer, String databaseUrl, String filesPath, String logPath) throws IOException {
        super(port, backupServer, filesPath, logPath);
        this.databaseUrl = databaseUrl;
    }

    protected Runnable newRunnable(Socket connection){
        this.out("Main Server: Creating Server Thread");

        SocketConnection socketConnection = new SocketConnection(connection);
        FileClient backupConnection = new FileClient(this.backupServer.getHost(), this.backupServer.getPort());

        return new MainServerConnection(
                socketConnection,
                backupConnection,
                this.databaseUrl,
                this.fileManager,
                this.logManager
        );
    }
}

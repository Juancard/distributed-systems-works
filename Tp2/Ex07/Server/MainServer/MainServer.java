package Tp2.Ex07.Server.MainServer;

import Common.ServerInfo;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Common.FileClient;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class MainServer extends Tp2.Ex01.Server.MainServer.MainServer{

    private String databaseUrl;

    public MainServer(int port, ServerInfo backupServer, String databaseUrl, File filesPath) {
        super(port, backupServer, filesPath);
        this.databaseUrl = databaseUrl;
    }

    protected Runnable newRunnable(Socket connection) throws IOException {
        this.out("Main Server. Starting Connection");
        SocketConnection socketConnection = new SocketConnection(connection);
        FileClient backupConnection = null;
        try {
            backupConnection = new FileClient(this.backupServer.getHost(), this.backupServer.getPort());
        } catch (IOException e) {
            String m = "Could not connect to backup server. Cause: " + e.getMessage();
            this.out(m);
            throw new IOException(m);
        }
        this.out("Main Server. Connected to backup " + backupConnection.getIdentity());

        return new MainServerConnection(
                socketConnection,
                backupConnection,
                this.databaseUrl,
                this.fileManager,
                this.logManager
        );
    }
}

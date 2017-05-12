package Tp2.Ex01.Server.MainServer;

import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Common.FileClient;
import Tp2.Ex01.Server.Common.FileServer;
import Common.Socket.SocketConnection;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class MainServer extends FileServer implements Runnable{

    protected ServerInfo backupServer;

    public MainServer(int port, ServerInfo backupServer, String filesPath, String logPath) throws IOException {
        super(port, filesPath, logPath);
        this.backupServer = backupServer;
    }

    protected Runnable newRunnable(Socket connection) throws IOException {
        this.out("Main Server: Creating Server Thread");

        SocketConnection socketConnection = new SocketConnection(connection);
        FileClient backupConnection = null;
        try {
            backupConnection = new FileClient(backupServer.getHost(), backupServer.getPort());
        } catch (IOException e) {
            String m = "Could not connect to backup server. Cause: " + e.getMessage();
            throw new IOException(m);
        }

        return new MainServerConnection(
                socketConnection,
                backupConnection,
                this.fileManager,
                this.logManager
        );
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (IOException e) {e.printStackTrace();}
    }
}

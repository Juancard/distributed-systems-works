package Tp2.Ex01.Server.MainServer;

import Tp2.Ex01.Server.Common.FileServer;
import Common.Socket.SocketConnection;

import java.io.*;
import java.net.Socket;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class MainServer extends FileServer {

    public MainServer(int port, String filesPath, String logPath) throws IOException {
        super(port, filesPath, logPath);
    }

    protected Runnable newRunnable(Socket connection){
        this.out("Main Server: Creating Server Thread");
        return new MainServerConnection(new SocketConnection(connection), this.fileManager, this.logManager);
    }
}

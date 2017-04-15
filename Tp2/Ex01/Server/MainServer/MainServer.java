package Tp2.Ex01.Server.MainServer;

import Tp2.Ex01.Server.Common.FileServer;
import Tp2.Ex01.Server.Common.FileServerThread;
import Tp2.Ex01.Common.SocketConnection;

import java.io.*;
import java.net.Socket;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class MainServer extends FileServer {

    private static final String FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/MainServer/Resources/Files/";
    private static final String LOG_FILE_PATH = "distributed-systems-works/Tp2/Ex01/Server/MainServer/Resources/Log/main_server_log.txt";

    public MainServer(int port) throws IOException {
        super(port, FILES_PATH, LOG_FILE_PATH);
    }

    protected Runnable newRunnableThread(Socket connection){
        this.out("Main Server: Creating Server Thread");
        return new MainServerConnection(new SocketConnection(connection), this.fileManager, this.logManager);
    }
}

package Tp2.Ex01.Server.FileServer;

import Common.CommonMain;
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
public class FileServerWithBackup extends FileServer {

    private static final String FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/FileServer/Resources/Files/";
    private static final String LOG_FILE_PATH = "distributed-systems-works/Tp2/Ex01/Server/FileServer/Resources/Log/file_server_log.txt";

    public FileServerWithBackup(int port) throws IOException {
        super(port, FILES_PATH, LOG_FILE_PATH);
    }

    protected FileServerThread newFileServerThread(Socket connection){
        this.out("File Server with backup: Creating Server Thread");
        return new ServerThread(new SocketConnection(connection), this.fileManager, this.logManager);
    }
}

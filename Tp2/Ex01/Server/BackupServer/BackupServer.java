package Tp2.Ex01.Server.BackupServer;

import Tp2.Ex01.Common.SocketConnection;
import Tp2.Ex01.Server.Common.FileServer;
import Tp2.Ex01.Server.Common.FileServerThread;

import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class BackupServer extends FileServer{

    private static final String FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/BackupServer/Resources/Files/";
    private static final String LOG_FILE_PATH = "distributed-systems-works/Tp2/Ex01/Server/BackupServer/Resources/Log/backup_server_log.txt";

    public BackupServer(int port) throws IOException {
        super(port, FILES_PATH, LOG_FILE_PATH);
    }

    protected FileServerThread newFileServerThread(Socket connection){
        this.out("Backup Server: Creating Backup Connection");
        return new BackupConnection(new SocketConnection(connection), this.fileManager, this.logManager);
    }

}

package Tp2.Ex01.Server.BackupServer;

import Common.Socket.SocketConnection;
import Tp2.Ex01.Server.Common.FileServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:53
 */
public class BackupServer extends FileServer implements Runnable{

    public BackupServer(int port, File filesPath) {
        super(port, filesPath);
    }

    protected Runnable newRunnable(Socket connection){
        this.out("Backup Server: Creating Backup Connection");
        return new BackupConnection(new SocketConnection(connection), this.fileManager, this.logManager);
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

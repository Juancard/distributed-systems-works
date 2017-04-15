package Tp2.Ex01.Server.FileServer;

import Tp2.Ex01.Common.FileClient;
import Tp2.Ex01.Common.FileProtocol;
import Tp2.Ex01.Common.TextFile;
import Tp2.Ex01.Server.Common.FileManager;
import Tp2.Ex01.Server.Common.FileServerThread;
import Tp2.Ex01.Server.Common.LogManager;
import Tp2.Ex01.Common.SocketConnection;

import java.io.*;
import java.net.SocketException;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class ServerThread extends FileServerThread implements Runnable {

    private static final int BACKUP_SERVER_PORT = 5121;
    private static final String BACKUP_SERVER_HOST = "localhost";

    private FileClient backupConnection;

    public ServerThread(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);
        this.backupConnection = new FileClient(BACKUP_SERVER_HOST, BACKUP_SERVER_PORT);
    }

    protected boolean del() throws IOException, ClassNotFoundException {
        String fileName = this.clientConnection.read().toString();
        boolean delResult = fileManager.del(fileName);
        if (delResult) backupConnection.del(fileName);
        return delResult;

    }

    protected boolean post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.clientConnection.read();
        boolean postResult = fileManager.post(textFile);
        if (postResult) backupConnection.post(textFile.getName(), textFile.getContent());
        return postResult;
    }

    public void close(){
        this.backupConnection.close();
        this.clientConnection.close();
    }
}

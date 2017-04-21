package Tp2.Ex01.Server.MainServer;

import Tp2.Ex01.Common.FileClient;
import Common.TextFile;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Server.Common.FileWorker;
import Tp2.Ex01.Server.Common.LogManager;

import java.io.*;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends FileWorker implements Runnable {

    private static final int BACKUP_SERVER_PORT = 5121;
    private static final String BACKUP_SERVER_HOST = "localhost";

    private FileClient backupClient;

    public MainServerConnection(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);
        this.backupClient = new FileClient(BACKUP_SERVER_HOST, BACKUP_SERVER_PORT);

    }

    protected boolean del() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        boolean delResult = fileManager.del(fileName);
        if (delResult) backupClient.del(fileName);
        return delResult;

    }

    protected boolean post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.readFromClient();
        boolean postResult = fileManager.post(textFile);
        if (postResult) backupClient.post(textFile.getName(), textFile.getContent());
        return postResult;
    }

    public void close(){
        this.backupClient.close();
        super.close();
    }
}

package Tp2.Ex01.Server.MainServer;

import Common.PropertiesManager;
import Tp2.Ex01.Common.FileClient;
import Common.TextFile;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Server.Common.FileWorker;
import Tp2.Ex01.Server.Common.LogManager;

import java.io.*;
import java.util.Properties;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends FileWorker implements Runnable {

    private FileClient backupConnection;

    public MainServerConnection(SocketConnection clientConnection, FileClient backupConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);
        this.backupConnection = backupConnection;
    }

    protected boolean del() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        boolean delResult = fileManager.del(fileName);
        if (delResult) backupConnection.del(fileName);
        return delResult;
    }

    protected boolean post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.readFromClient();
        boolean postResult = fileManager.post(textFile);
        if (postResult) backupConnection.post(textFile.getName(), textFile.getContent());
        return postResult;
    }

    public void close(){
        this.backupConnection.close();
        super.close();
    }
}

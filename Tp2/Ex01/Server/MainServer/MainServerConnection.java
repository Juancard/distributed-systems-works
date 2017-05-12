package Tp2.Ex01.Server.MainServer;

import Common.FileException;
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

    protected Object del() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        try {
            boolean delResult = fileManager.del(fileName);
            if (delResult)
                delResult = backupConnection.del(fileName);
            return delResult;
        } catch(FileException e){
            this.display(e.getMessage());
            return e;
        } catch (Exception e) {
            e.printStackTrace();
            this.display(e.getMessage());
            return false;
        }
    }

    protected boolean post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.readFromClient();
        boolean postResult = fileManager.post(textFile);
        if (postResult) try {
            backupConnection.post(textFile.getName(), textFile.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            this.display(e.getMessage());
            return false;
        }
        return postResult;
    }

    public void close(){
        this.backupConnection.close();
        super.close();
    }
}

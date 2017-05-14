package Tp2.Ex01.Server.MainServer;

import Common.FileException;
import Tp2.Ex01.Common.FileClient;
import Common.TextFile;
import Common.FileManager;
import Common.Socket.SocketConnection;
import Tp2.Ex01.Server.Common.FileWorker;
import Common.LogManager;

import java.io.*;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class MainServerConnection extends FileWorker implements Runnable {

    private FileClient backupConnection;

    public MainServerConnection(SocketConnection clientConnection, FileClient backupConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager);
        this.logManager = logManager;
        this.backupConnection = backupConnection;
    }

    protected Object del() throws IOException, ClassNotFoundException {
        String fileName = this.readFromClient().toString();
        return this.del(fileName);
    }

    protected Object del(String filename){
        try {
            boolean delResult = fileManager.del(filename);
            if (delResult)
                delResult = backupConnection.del(filename);
            return delResult;
        } catch(FileException e){
            return e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected Object post() throws IOException, ClassNotFoundException {
        TextFile textFile = (TextFile) this.readFromClient();
        return this.post(textFile);
    }

    protected Object post(TextFile textFile){
        Object postResult;

        try {
            postResult = fileManager.post(textFile);
            if ((Boolean) postResult)
                postResult = backupConnection.post(textFile.getName(), textFile.getContent());
        } catch (FileException e) {
            postResult = e;
        } catch (Exception e) {
            e.printStackTrace();
            postResult = e;
        }

        return postResult;
    }

    public void close(){
        this.backupConnection.close();
        super.close();
    }
}

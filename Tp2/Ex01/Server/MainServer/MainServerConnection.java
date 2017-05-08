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

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex01/config.properties";

    private FileClient backupClient;

    public MainServerConnection(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);

        Properties properties = null;
        try {
            properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        } catch (IOException e) {e.printStackTrace();}

        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));

        this.backupClient = new FileClient(backupHost, backupPort);
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

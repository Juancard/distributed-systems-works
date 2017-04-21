package Tp2.Ex01.Server.BackupServer;

import Common.Socket.SocketConnection;
import Common.FileManager;
import Tp2.Ex01.Server.Common.FileWorker;
import Tp2.Ex01.Server.Common.LogManager;


/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class BackupConnection extends FileWorker implements Runnable{

    public BackupConnection(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);
    }

}

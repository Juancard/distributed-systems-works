package Tp2.Ex01.Server.BackupServer;

import Tp2.Ex01.Common.FileProtocol;
import Tp2.Ex01.Common.SocketConnection;
import Tp2.Ex01.Common.TextFile;
import Tp2.Ex01.Server.Common.FileManager;
import Tp2.Ex01.Server.Common.FileServerThread;
import Tp2.Ex01.Server.Common.LogManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 13:56
 */
public class BackupConnection extends FileServerThread implements Runnable{

    public BackupConnection(SocketConnection clientConnection, FileManager fileManager, LogManager logManager) {
        super(clientConnection, fileManager, logManager);
    }

}

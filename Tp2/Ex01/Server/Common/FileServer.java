package Tp2.Ex01.Server.Common;

import Common.FileManager;
import Common.Socket.MyCustomServer;
import Common.Socket.SocketConnection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 22:48
 */
public class FileServer extends MyCustomServer{
    protected FileManager fileManager;
    protected LogManager logManager;

    public FileServer(int port, String filesPath) throws IOException {
        super(port);
        this.prepareFileServer(filesPath);
    }

    public FileServer(int port, String filesPath, String logFilePath) throws IOException {
        super(port);
        this.prepareFileServer(filesPath);
        this.setLogFile(logFilePath);
    }

    public void setLogFile(String logFilePath) throws FileNotFoundException {
        this.logManager.setLogPrinter(new PrintStream(new FileOutputStream(logFilePath, true)));
        System.out.println("Logs in: " + logFilePath);
    }

    private void prepareFileServer(String filesPath) throws IOException {
        this.fileManager = new FileManager(filesPath);
        PrintStream logPrinter = System.out;
        this.logManager = new LogManager(logPrinter);
    }

    protected Runnable newRunnable(Socket connection){
        return new FileWorker(new SocketConnection(connection), this.fileManager, this.logManager);
    }

    public void out(String toPrint){
        this.logManager.log(toPrint);
    }
}

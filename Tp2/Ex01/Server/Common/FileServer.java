package Tp2.Ex01.Server.Common;

import Common.FileManager;
import Common.Socket.MyCustomServer;
import Common.Socket.SocketConnection;

import java.io.*;
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
        File f = new File(logFilePath);
        if (f.isDirectory()) {
            f = new File(logFilePath + "server_" + this.getPort() + ".log");
            if (!(f.exists()))
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        this.logManager.setLogPrinter(new PrintStream(new FileOutputStream(f, true)));
        System.out.println("Logs in: " + f.getPath());
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

package Tp2.Ex01.Server.Common;

import Common.FileManager;
import Common.LogManager;
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

    public FileServer(int port, String filesPath) throws IOException {
        super(port);
        this.prepareFileServer(filesPath);
    }

    public FileServer(int port, String filesPath, PrintStream logWriter) throws IOException {
        super(port);
        this.prepareFileServer(filesPath);
        this.logManager.setLogPrinter(logWriter);
    }

    private void prepareFileServer(String filesPath) throws IOException {
        this.fileManager = new FileManager(filesPath);
    }

                               /*
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
        this.setLogWriter(new PrintStream(new FileOutputStream(f, true)));
        System.out.println("Logs in: " + f.getPath());
    }               */

    protected Runnable newRunnable(Socket connection) throws IOException {
        FileWorker fileWorker = new FileWorker(new SocketConnection(connection), this.fileManager);
        fileWorker.setLogManager(this.logManager);
        return fileWorker;
    }

    public void out(String toPrint){
        this.logManager.log(toPrint);
    }
}

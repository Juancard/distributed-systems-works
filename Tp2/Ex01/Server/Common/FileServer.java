package Tp2.Ex01.Server.Common;

import Common.FileManager;
import Tp2.Ex01.Common.SocketConnection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 22:48
 */
public class FileServer {
    private int port;
    private ServerSocket serverSocket;
    private List<Runnable> threadsPool;
    protected FileManager fileManager;
    protected LogManager logManager;

    public FileServer(int port, String filesPath) throws IOException {
        this.prepareServer(port, filesPath);
        this.startServer();
    }

    public FileServer(int port, String filesPath, String logFilePath) throws IOException {
        this.prepareServer(port, filesPath);
        this.setLogFile(logFilePath);
        this.startServer();
    }

    private void prepareServer(int port, String filesPath) throws IOException {
        this.port = port;
        this.threadsPool = new ArrayList<Runnable>();
        this.fileManager = new FileManager(filesPath);
        PrintStream logPrinter = System.out;
        this.logManager = new LogManager(logPrinter);
    }

    private void startServer() {
        this.instantiateServer();
        this.handleConnections();
    }

    private void instantiateServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            String toPrint = String.format("Listening on port: %d...", this.port);
            this.out(toPrint);
        } catch (IOException e) {
            this.out("Error in creating new server socket");
            this.closeServer();
        }
    }

    private void handleConnections() {
        Socket connection;
        while (!this.serverSocket.isClosed()){
            try {
                connection = this.serverSocket.accept();
                this.newConnection(connection);
            } catch (IOException e) {
                this.out("IOException: Error in establishing connection with client");
            }
        }
    }

    private void newConnection(Socket connection) {
        Runnable serverThread = this.newRunnableThread(connection);
        this.threadsPool.add(serverThread);
        new Thread(serverThread).start();
        String toPrint = "New connection with client: " + connection.getRemoteSocketAddress();
        this.out(toPrint);
    }

    protected Runnable newRunnableThread(Socket connection){
        return new FileServerThread(new SocketConnection(connection), this.fileManager, this.logManager);
    }

    private void closeServer() {
        Iterator i = this.threadsPool.iterator();
        while (i.hasNext()){
            ((FileServerThread) i.next()).close();
        }
        this.threadsPool.clear();
    }

    public void setLogFile(String logFilePath) throws FileNotFoundException {
        this.logManager.setLogPrinter(new PrintStream(new FileOutputStream(logFilePath, true)));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void out(String toPrint){
        System.out.println(toPrint);
        this.logManager.log(toPrint);
    }
}

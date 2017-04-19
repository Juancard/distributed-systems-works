package Tp2.Ex02.Server;

import Common.Socket.MyCustomServer;

import java.net.Socket;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:02
 */
public class BankServer extends MyCustomServer {

    public BankServer(int port) {
        super(port);
    }

    @Override
    protected Runnable newRunnable(Socket clientSocket) {
        return new BankWorker(clientSocket);
    }
}

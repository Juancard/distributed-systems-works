package Tp2.Ex02.Server;

import Common.Socket.MyCustomServer;

import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:02
 */
public class BankServer extends MyCustomServer  implements Runnable{

    private AccountsManager accountsManager;

    public BankServer(int port, AccountsManager accountsManager) throws IOException {
        super(port);
        this.accountsManager = accountsManager;
    }

    @Override
    protected Runnable newRunnable(Socket clientSocket) {
        return new BankWorker(clientSocket, this.accountsManager);
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Tp2.Ex02.Server;

import Common.Socket.MyCustomServer;

import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:02
 */
public class BankServer extends MyCustomServer {

    private static final String ACCOUNTS_PATH = "distributed-systems-works/Tp2/Ex02/Server/Resources/Accounts";

    private AccountsManager accountsManager;

    public BankServer(int port) throws IOException {
        super(port);
        this.accountsManager = new AccountsManager(ACCOUNTS_PATH);
        this.startServer();
    }

    @Override
    protected Runnable newRunnable(Socket clientSocket) {
        return new BankWorker(clientSocket, this.accountsManager);
    }
}

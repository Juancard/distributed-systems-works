package Tp2.Ex02.Client;

import Common.Socket.SocketConnection;
import Tp2.Ex02.Common.AccountProtocol;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 */
public class AccountClient{
    private String bankHost;
    private int portToDeposit;
    private int portToExtract;

    public AccountClient(String bankHost, int portToDeposit, int portToExtract) {
        this.bankHost = bankHost;
        this.portToDeposit = portToDeposit;
        this.portToExtract = portToExtract;

    }

    public double deposit(String accountOwner, double amountToDeposit) throws Exception {
        SocketConnection connection = this.depositConnection();
        connection.send(AccountProtocol.DEPOSIT);
        connection.send(accountOwner);
        connection.send(amountToDeposit);

        Object response = connection.read();
        if (response instanceof Exception)
            throw (Exception) response;

        connection.close();
        return (Double) response;
    }

    public double extract(String accountOwner, double amountToExtract) throws Exception {
        SocketConnection connection = this.extractConnection();
        connection.send(AccountProtocol.EXTRACT);
        connection.send(accountOwner);
        connection.send(amountToExtract);

        Object response = connection.read();
        if (response instanceof Exception)
            throw (Exception) response;

        connection.close();
        return (Double) response;
    }

    private SocketConnection depositConnection(){
        return new SocketConnection(this.bankHost, this.portToDeposit);
    }

    private SocketConnection extractConnection(){
        return new SocketConnection(this.bankHost, this.portToExtract);
    }
}

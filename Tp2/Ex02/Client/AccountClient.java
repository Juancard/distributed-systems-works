package Tp2.Ex02.Client;

import Common.Socket.SocketConnection;
import Tp2.Ex02.Common.AccountProtocol;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 */
public class AccountClient extends SocketConnection {

    public AccountClient(String host, int port) {
        super(host, port);
    }

    public double deposit(String accountOwner, double amountToDeposit) throws Exception {
        this.send(AccountProtocol.DEPOSIT);
        this.send(accountOwner);
        this.send(amountToDeposit);

        Object response = this.read();
        if (response instanceof Exception)
            throw (Exception) response;
        return (Double) response;
    }

    public double extract(String accountOwner, double amountToExtract) throws Exception {
        this.send(AccountProtocol.EXTRACT);
        this.send(accountOwner);
        this.send(amountToExtract);

        Object response = this.read();
        if (response instanceof Exception)
            throw (Exception) response;
        return (Double) response;
    }

}

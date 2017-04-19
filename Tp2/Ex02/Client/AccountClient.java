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

    public String authenticate(String accountOwner){
        this.send(accountOwner);
        String authenticationState = this.read().toString();

        boolean isAuthenticated = authenticationState.equals(AccountProtocol.AUTHENTICATION_OK);
        if (isAuthenticated) authenticationState = "";

        return authenticationState;
    }

    public double deposit(double amountToDeposit) throws Exception {
        this.send(AccountProtocol.DEPOSIT);
        this.send(amountToDeposit);

        Object response = this.read();
        if (response instanceof Exception)
            throw (Exception) response;
        return (Double) response;
    }

    public double extract(double amountToExtract) throws Exception {
        this.send(AccountProtocol.EXTRACT);
        this.send(amountToExtract);

        Object response = this.read();
        if (response instanceof Exception)
            throw (Exception) response;
        return (Double) response;
    }

    public Object read(){
        try {
            return super.read();
        } catch (Exception e) {
            e.printStackTrace();
            this.out("Error in reading object from socket. Closing socket: " + this.getIdentity());
            this.close();
            return null;
        }
    }

}

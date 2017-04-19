package Tp2.Ex02.Server;

import Common.Socket.MyCustomWorker;
import Tp2.Ex02.Common.AccountProtocol;
import Tp2.Ex02.Common.BankException;

import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:04
 */
public class BankWorker extends MyCustomWorker{

    private AccountProtocol accountProtocol;
    private BankAccount userBankAccount;

    public BankWorker(Socket clientSocket) {
        super(clientSocket);
        this.accountProtocol = new AccountProtocol();
    }

    // This is what gets called from parent class on RUN.
    protected void handleClientInput(Object clientInput){
        Object objectToClient = this.handleProtocolStates(clientInput);
        if (objectToClient != null)
            this.sendToClient(objectToClient);
    }

    private Object handleProtocolStates(Object clientInput) {
        int protocolState = this.accountProtocol.getState();
        Object out = null;

        if (protocolState == AccountProtocol.AUTHENTICATING){
            out = this.handleAuthentication(clientInput.toString());
        } else if (protocolState == AccountProtocol.READY) {
            out = this.onClientRequest(clientInput.toString());
        }

        return out;
    }

    private Object onClientRequest(String request) {
            Object out = new Object();

            if (request.equals(AccountProtocol.DEPOSIT)) {
                out = this.deposit();
            } else if (request.equals(AccountProtocol.EXTRACT)){
                out = this.extract();
            }

            return out;
    }

    private Object deposit() {
        try {
            double amountToDeposit = (Double) this.readFromClient();
            return this.userBankAccount.deposit(amountToDeposit);
        } catch (BankException e){
            return new BankException("On deposit: " + e.getMessage());
        } catch (Exception e) {
            return new BankException("On deposit: No amount specified");
        }
    }

    private Object extract() {
        try {
            double amountToExtract = (Double) this.readFromClient();
            return this.userBankAccount.extract(amountToExtract);
        } catch (BankException e){
            return new BankException("On extraction: " + e.getMessage());
        } catch(Exception e) {
            return new BankException("On extraction: No amount specified");
        }
    }

    private String handleAuthentication(String givenUser) {
        if (givenUser.length() <= 0)
            return "Error: Username is empty";
        return authenticate(givenUser);
    }

    private String authenticate(String givenUser) {
        this.userBankAccount = this.setUserBankAccount(givenUser);
        accountProtocol.setState(AccountProtocol.READY);
        return  AccountProtocol.AUTHENTICATION_OK;
    }

    private BankAccount setUserBankAccount(String givenUser) {
        return new BankAccount(givenUser, 0);
    }
}

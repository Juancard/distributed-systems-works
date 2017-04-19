package Tp2.Ex02.Server;

import Common.Socket.MyCustomWorker;
import Tp2.Ex02.Common.AccountProtocol;
import Tp2.Ex02.Common.BankException;

import java.net.Socket;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 14:04
 */
public class BankWorker extends MyCustomWorker{

    private AccountProtocol accountProtocol;
    private BankAccount userBankAccount;
    private AccountsManager accountsManager;

    public BankWorker(Socket clientSocket, AccountsManager accountsManager) {
        super(clientSocket);
        this.accountProtocol = new AccountProtocol();
        this.accountsManager = accountsManager;

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
            // Exercise says:
            // "deposito: proceso tarda 40 mseg entre que consulta el saldo actual y lo actualiza con nuevo valor"
            Thread.sleep(40);
            double resultingBalance = this.userBankAccount.deposit(amountToDeposit);
            accountsManager.add(this.userBankAccount);
            return resultingBalance;
        } catch (BankException e){
            return new BankException("On deposit: " + e.getMessage());
        } catch (Exception e) {
            return new BankException("On deposit: No amount specified");
        }
    }

    private Object extract() {
        try {
            double amountToExtract = (Double) this.readFromClient();
            // Exercise says:
            // "extraccion: proceso tarda 80 mseg entre que consulta el saldo (verifica que haya disponible)
            // y lo actualiza con nuevo valor"
            Thread.sleep(80);
            double resultingBalance = this.userBankAccount.extract(amountToExtract);
            accountsManager.add(this.userBankAccount);
            return resultingBalance;
        } catch (BankException e){
            return new BankException("On extraction: " + e.getMessage());
        } catch(Exception e) {
            return new BankException("On extraction: No amount specified");
        }
    }

    private String handleAuthentication(String givenUser) {
        if (givenUser.length() <= 0)
            return "Error: Username is empty";
        if (givenUser.contains("#"))
            return "Error: character '#' is not valid for username";

        try {
            return authenticate(givenUser);
        } catch (BankException e) {
            return e.getMessage();
        }
    }

    private String authenticate(String givenUser) throws BankException {
        this.userBankAccount = this.setUserBankAccount(givenUser);
        accountProtocol.setState(AccountProtocol.READY);
        return  AccountProtocol.AUTHENTICATION_OK;
    }

    private BankAccount setUserBankAccount(String givenUser) throws BankException {
        try {
            BankAccount userAccount = accountsManager.getByUsername(givenUser);
            return userAccount;
        } catch (BankException e) {
            return this.createNewAccount(givenUser);
        }
    }

    private BankAccount createNewAccount(String username) throws BankException {
        System.out.println("Creating new account for user: " + username);
        BankAccount newBankAccount = BankAccount.openNew(username);
        if (accountsManager.add(newBankAccount))
            return newBankAccount;
        else
            throw new BankException("Could not create new account for user '" + username);
    }
}

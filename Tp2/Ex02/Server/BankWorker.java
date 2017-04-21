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
    private AccountsManager accountsManager;

    public BankWorker(Socket clientSocket, AccountsManager accountsManager) {
        super(clientSocket);
        this.accountProtocol = new AccountProtocol();
        this.accountsManager = accountsManager;

    }

    // This is what gets called from parent class on RUN.
    protected void handleClientInput(Object clientInput){
        Object objectToClient = this.onClientRequest(clientInput.toString());
        if (objectToClient != null)
            this.sendToClient(objectToClient);
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
        this.display(this.clientIdentity() + " - Calls deposit");
        try {

            // Reads deposit parameters
            String accountOwner = this.readFromClient().toString();
            double amountToDeposit = (Double) this.readFromClient();
            this.display(this.clientIdentity() + " - Parameters: " + accountOwner + " - " + amountToDeposit);

            synchronized (this.accountsManager) {
                // Exercise says:
                // "deposito: proceso tarda 40 mseg entre que consulta el saldo actual y lo actualiza con nuevo valor"
                Thread.sleep(40);

                // Retrieves account for user in parameter
                BankAccount bankAccount;
                if (this.accountsManager.hasAccount(accountOwner)) {
                    bankAccount = this.accountsManager.getByOwner(accountOwner);
                } else {
                    bankAccount = this.createNewAccount(accountOwner);
                }
                this.display(this.clientIdentity() + " - Current balance of " + bankAccount.getOwner() + " is: " + bankAccount.getBalance());

                // Performs deposit
                double resultingBalance = bankAccount.deposit(amountToDeposit);
                this.display(this.clientIdentity() + " - Deposit finished, new balance of " + bankAccount.getOwner() + " is: " + bankAccount.getBalance());

                // Persists updated balance
                accountsManager.add(bankAccount);

                return resultingBalance;
            }
        } catch (Exception e){
            return new BankException("On deposit: " + e.getMessage());
        }
    }

    private Object extract() {
        try {
            // read parameters
            String accountOwner = this.readFromClient().toString();
            double amountToExtract = (Double) this.readFromClient();

            synchronized (this.accountsManager) {
                // Exercise says:
                // "extraccion: proceso tarda 80 mseg entre que consulta el saldo (verifica que haya disponible)
                // y lo actualiza con nuevo valor"
                Thread.sleep(80);

                // Retrieves account for user in parameter
                if (! this.accountsManager.hasAccount(accountOwner))
                    throw new BankException("No account for specified owner");
                BankAccount bankAccount = this.accountsManager.getByOwner(accountOwner);

                // Performs extraction
                double resultingBalance = bankAccount.extract(amountToExtract);

                // Persists updated balance
                accountsManager.add(bankAccount);

                return resultingBalance;
            }
        } catch (BankException e){
            return new BankException("On extraction: " + e.getMessage());
        } catch(Exception e) {
            return new BankException("On extraction: No amount specified");
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

package Tp2.Ex02.Server;

import Common.FileException;
import Common.FileManager;
import Common.TextFile;
import Tp2.Ex02.Common.BankException;

import java.io.*;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 18:04
 */
public class AccountsManager extends FileManager{

    public AccountsManager(String filesPathString) throws IOException {
        super(filesPathString);
    }

    public boolean add(BankAccount account){
        System.out.println("Persisting: " + account);
        return this.post(this.accountToTextFile(account));
    }

    public BankAccount getByOwner(String owner) throws BankException {
        try {
            TextFile userFile = this.get(owner);
            return this.textFileToAccount(userFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BankException("Could not get username account: " + e.getMessage());
        } catch (FileException e) {
            throw new BankException("Could not get username account: " + e.getMessage());
        }
    }

    public boolean hasAccount(String owner){
        return this.exists(owner);
    }

    protected TextFile accountToTextFile(BankAccount account) {
        String filename = account.getOwner();
        String content = Double.toString(account.getBalance());
        return new TextFile(filename, content);
    }

    protected BankAccount textFileToAccount(TextFile textFile) throws BankException{
        String owner = textFile.getName();
        String content = textFile.getContent();

        double balance;
        if (content == null || content.length() <= 0)
            throw new BankException("Account found but is not valid");
        try {
            balance = Double.parseDouble(content);
        } catch (NumberFormatException e) {
            throw new BankException("Amount found on account is not a valid number");
        }

        return new BankAccount(owner, balance);
    }

}

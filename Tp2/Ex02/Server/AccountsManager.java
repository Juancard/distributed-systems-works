package Tp2.Ex02.Server;

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
        return this.post(this.accountToTextFile(account));
    }

    public BankAccount getByUsername(String username) throws BankException {
        System.out.println("In get by username");
        try {
            TextFile userFile = this.get(username);
            System.out.println(userFile);
            return this.textFileToAccount(userFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BankException("Could not get username account: " + e.getMessage());
        }

    }

    private TextFile accountToTextFile(BankAccount account) {
        String filename = account.getOwner();
        String content = Double.toString(account.getBalance());
        return new TextFile(filename, content);
    }

    private BankAccount textFileToAccount(TextFile textFile) throws BankException{
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

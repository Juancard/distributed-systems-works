package Tp2.Ex03.Server.Bank;

import Common.TextFile;
import Tp2.Ex01.Common.FileClient;
import Tp2.Ex02.Common.BankException;
import Tp2.Ex02.Server.AccountsManager;
import Tp2.Ex02.Server.BankAccount;

import java.io.IOException;

/**
 * User: juan
 * Date: 22/04/17
 * Time: 13:38
 */
public class AccountsManagerWithBackup extends AccountsManager{

    private String backupHost;
    private int backupPort;

    public AccountsManagerWithBackup(String backupHost, int backupPort, String filesPathString) throws IOException {
        super(filesPathString);
        this.backupHost = backupHost;
        this.backupPort = backupPort;
    }

    public boolean add(BankAccount account) throws BankException {
        if (!super.add(account)) return false;
        return this.backupPost(this.accountToTextFile(account));
    }

    private boolean backupPost(TextFile textFile) throws BankException {
        FileClient fileClient = null;
        try {
            fileClient = this.connectToBackup();
        } catch (IOException e) {
            throw new BankException(e.getMessage());
        }
        boolean result = false;
        try {
            result = fileClient.post(textFile.getName(), textFile.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            fileClient.close();
            return result;
        }
    }

    public TextFile get(String owner) throws IOException {
        FileClient fileClient = this.connectToBackup();
        String content = null;
        try {
            content = fileClient.get(owner);
        } catch (Exception e) {
            e.printStackTrace();
           content = "";
        }
        fileClient.close();
        return new TextFile(owner, content);
    }

    public FileClient connectToBackup() throws IOException {
        try {
            return new FileClient(this.backupHost, this.backupPort);
        } catch (IOException e) {
            String m = "Could not connect to backup server. Cause: " + e.getMessage();
            throw new IOException(m);
        }
    }
}

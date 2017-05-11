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
public class BackupAccountManager extends AccountsManager{

    private String backupHost;
    private int backupPort;

    public BackupAccountManager(String backupHost, int backupPort, String filesPathString) throws IOException {
        super(filesPathString);
        this.backupHost = backupHost;
        this.backupPort = backupPort;
    }

    public boolean add(BankAccount account){
        if (!super.add(account)) return false;
        return this.backupPost(this.accountToTextFile(account));
    }

    private boolean backupPost(TextFile textFile){
        FileClient fileClient = new FileClient(this.backupHost, this.backupPort);
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

    public TextFile get(String owner){
        FileClient fileClient = new FileClient(this.backupHost, this.backupPort);
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
}

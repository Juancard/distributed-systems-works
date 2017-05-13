package Tp2.Ex07.Client;

import Common.FileException;
import Common.TextFile;
import Tp2.Ex07.Common.FileProtocol;
import Tp2.Ex07.Common.LoginException;
import Tp2.Ex07.Common.PermissionException;
import Tp2.Ex07.Common.User;

import java.io.IOException;

/**
 * User: juan
 * Date: 08/05/17
 * Time: 15:21
 */
public class FileClient extends Tp2.Ex01.Common.FileClient {

    public FileClient(String host, int port) throws IOException {
        super(host, port);
    }

    public void login(String username, String password) throws LoginException, IOException, ClassNotFoundException {
        User user = new User(username, password);
        this.send(FileProtocol.LOGIN);
        this.send(user);

        Object loginResult = this.read();
        if (loginResult instanceof LoginException){
            throw (LoginException) loginResult;
        }
    }

    @Override
    public boolean post(String fileName, String fileContent) throws PermissionException, FileException, IOException, ClassNotFoundException {
        this.send(FileProtocol.POST);
        this.send(new TextFile(fileName, fileContent));
        Object in = this.read();

        if (in instanceof PermissionException)
            throw (PermissionException) in;
        if (in instanceof FileException)
            throw (FileException) in;

        return (Boolean) in;
    }

    @Override
    public String get(String fileName) throws PermissionException, FileException, IOException, ClassNotFoundException {
        this.send(FileProtocol.GET);
        this.send(fileName);
        Object in = this.read();

        if (in instanceof PermissionException)
            throw (PermissionException) in;
        if (in instanceof FileException)
            throw (FileException) in;

        TextFile textFile = (TextFile) in;
        return textFile.getContent();
    }
}

package Tp2.Ex07.Client;

import Tp2.Ex07.Common.FileProtocol;
import Tp2.Ex07.Common.LoginException;
import Tp2.Ex07.Common.User;

/**
 * User: juan
 * Date: 08/05/17
 * Time: 15:21
 */
public class FileClient extends Tp2.Ex01.Common.FileClient {

    private User loggedUser;

    public FileClient(String host, int port) {
        super(host, port);
    }

    public void login(String username, String password) throws LoginException {
        User user = new User(username, password);
        this.send(FileProtocol.LOGIN);
        this.send(user);

        Object loginResult = this.read();
        if (loginResult instanceof LoginException){
            throw (LoginException) loginResult;
        }
    }
}

package Tp2.Ex07.Client;

import Tp2.Ex07.User;

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

    public boolean logIn(String username, String password){
        User user = new User(username, password);
        return true;
    }
}

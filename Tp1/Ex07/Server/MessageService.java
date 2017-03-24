package Tp1.Ex07.Server;


import Tp1.Ex03.Common.Message;
import Tp1.Ex03.Server.MessagesHandler;
import Tp1.Ex07.Common.IMessageService;
import Tp1.Ex07.Common.NotValidUserException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class MessageService implements IMessageService {

    private MessagesHandler messagesHandler;

    public MessageService() {
        this.messagesHandler = new MessagesHandler();
    }

    @Override
    public void sendNewMessage(Message message) throws RemoteException {
        this.messagesHandler.addMessage(message);
    }

    @Override
    public List<Message> readMessagesSentTo(String user) throws RemoteException {
        return this.messagesHandler.readMessagesSentTo(user);
    }

    @Override
    public boolean authenticate(String givenUser) throws RemoteException, NotValidUserException {
        if (givenUser.length() <= 0) throw new NotValidUserException("Username is empty");
        return true;
    }
}

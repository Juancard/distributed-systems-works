package Tp1.Ex07.Common;

import Tp1.Ex03.Common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:23
 */
public interface IMessageService extends Remote {
    public static final String DNS_NAME = "MESSAGE_SERVICE";

    public void sendNewMessage(Message message) throws RemoteException;
    public List<Message> readMessagesSentTo(String user) throws RemoteException;
    public boolean authenticate(String user) throws RemoteException, NotValidUserException;
}

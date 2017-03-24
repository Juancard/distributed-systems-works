package Tp1.Ex07.Client;

import Tp1.Ex03.Common.Message;
import Tp1.Ex07.Common.IMessageService;
import Tp1.Ex07.Common.NotValidUserException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:00
 */
public class MessageClient {

    private IMessageService iMessageService;

    public MessageClient(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.iMessageService = (IMessageService) registry.lookup(IMessageService.DNS_NAME);
    }

    public void sendNewMessage(String body, String from, String to) throws RemoteException {
        this.iMessageService.sendNewMessage(new Message(body, from, to));
    }

    public List<Message> readMessagesSentTo(String user) throws RemoteException {
        return this.iMessageService.readMessagesSentTo(user);
    }

    public boolean authenticate(String user) throws RemoteException, NotValidUserException {
        return this.iMessageService.authenticate(user);
    }
}

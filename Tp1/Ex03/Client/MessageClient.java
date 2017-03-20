package Tp1.Ex03.Client;

import Tp1.Ex03.Common.Message;
import Tp1.Ex03.Common.MessageProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class MessageClient extends SocketClient {

    public MessageClient(String host, int port) {
        super(host, port);
    }

    public boolean sendNewMessageRequest(Message message) {
        this.sendToSocket(MessageProtocol.SEND_NEW_MESSAGE);
        this.sendToSocket(message);
        return isMessageAdded();
    }

    private boolean isMessageAdded() {
        Object response = this.readFromSocket();
        return response.equals(MessageProtocol.MESSAGE_ADDED);
    }

    public List<Message> sendReadMessagesRequest(){
        this.sendToSocket(MessageProtocol.READ_MESSAGES);
        return readMessagesFromServer();
    }

    private List<Message> readMessagesFromServer() {
        List<Message> messagesReceived = new ArrayList<Message>();

        Object read;
        while (true) {
            read = this.readFromSocket();
            if (read.toString().equals(MessageProtocol.READ_MESSAGES_END))
                break;
            messagesReceived.add((Message) read);
        }

        return messagesReceived;
    }

    public String sendAuthenticationRequest(String username){
        this.sendToSocket(username);
        String authenticationState = this.readFromSocket().toString();

        boolean isAuthenticated = this.checkAuthentication(authenticationState);
        if (isAuthenticated) authenticationState = "";

        return authenticationState;
    }

    public boolean checkAuthentication(String authenticationState) {
        return authenticationState.equals(MessageProtocol.AUTHENTICATION_OK);
    }

}

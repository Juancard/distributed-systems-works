package tp1.ej03.Client;

import tp1.ej03.Client.SocketClient;
import tp1.ej03.Message;
import tp1.ej03.MessageProtocol;

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
        this.sendToSocket(message);
        return isMessageSent();
    }

    private boolean isMessageSent() {
        Object response = this.readFromSocket();
        return response.equals(MessageProtocol.MESSAGE_SENT_OK);
    }

    public List<Message> sendReadMessagesRequest(){
        this.sendToSocket(MessageProtocol.READ_MESSAGES);
        List<Message> messagesReceived = (List<Message>) this.readFromSocket();
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

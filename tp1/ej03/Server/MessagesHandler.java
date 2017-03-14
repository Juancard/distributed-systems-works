package tp1.ej03.Server;

import tp1.ej03.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 14:38
 */
public class MessagesHandler {
    private HashMap<String, List<Message>> messages;

    public MessagesHandler() {
        this.messages = new HashMap<String, List<Message>>();
    }

    public void addMessage(Message message) {
        String destination = message.getTo();

        messages.putIfAbsent(destination, new ArrayList<Message>());
        messages.get(destination).add(message);

    }

    public List<Message> readMessagesSentTo(String user){
        List<Message> messagesReceived = this.messages.get(user);
        List<Message> out;
        if (messagesReceived == null){
            out = new ArrayList<Message>();
        } else {
            out = new ArrayList<Message>(messagesReceived);
        }
        return out;
    }
}

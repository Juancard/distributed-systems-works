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
        if (!this.messages.containsKey(user))
            return new ArrayList<Message>();
        return this.messages.get(user);
    }

    public void removeMessagesFrom(List<Message> toRemoveList, String username) {
        List<Message> allUserMessages = this.messages.get(username);
        for (Message messageToRemove : toRemoveList) {
            if (allUserMessages.contains(messageToRemove)){
                allUserMessages.remove(messageToRemove);
            }
        }
    }

}

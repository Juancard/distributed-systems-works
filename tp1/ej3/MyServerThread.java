package tp1.ej3;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class MyServerThread implements Runnable{

    private long threadId;
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private MessageProtocol messageProtocol;

    private String userAuthenticated;
    private HashMap<String, List<Message>> messages;

    public MyServerThread(Socket clientSocket, HashMap<String, List<Message>> messages) {
        this.clientSocket = clientSocket;
        this.messages = messages;
        try {
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.messageProtocol = new MessageProtocol();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!this.clientSocket.isClosed()){
                // It stays block until a new message arrive
                Object objectFromClient = readMessage();
                if (objectFromClient == null) break;

                Object objectToClient = messageProtocol.processInput(objectFromClient);
                int protocolState = messageProtocol.getState();

                if (protocolState == MessageProtocol.AUTHENTICATING){

                    objectToClient = this.authenticate((String) objectToClient);

                } else if (protocolState == MessageProtocol.READY) {

                    if (objectToClient instanceof Message) {
                        this.addMessage((Message) objectToClient);
                        objectToClient = MessageProtocol.MESSAGE_SENT_OK;
                    }  else if (objectToClient.toString().equals(MessageProtocol.READ_MESSAGES_RECEIVED)){
                        objectToClient = this.readMessagesSentTo(this.userAuthenticated);
                    }

                }
                System.out.println("Sending to CLient: " + objectToClient);
                this.sendMessage(objectToClient);
            }
        } catch (SocketException e) {
            System.out.println("Connection lost with client: " + this.clientSocket.getRemoteSocketAddress());
            this.close ();
        } catch (EOFException e) {
            System.out.println("A client has disconnected: " + this.clientSocket.getRemoteSocketAddress());
            this.close ();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Message> readMessagesSentTo(String userAuthenticated) throws IOException{
        List<Message> messagesReceived = this.messages.get(userAuthenticated);
        List<Message> out;
        if (messagesReceived == null){
            out = new ArrayList<Message>();
        } else {
            out = new ArrayList<Message>(messagesReceived);
        }
        return out;
    }

    private void addMessage(Message message) {
        String destination = message.getDestination();

        messages.putIfAbsent(destination, new ArrayList<Message>());
        messages.get(destination).add(message);

        System.out.println("Message Added: \n - " + message);
    }

    private String authenticate(String givenUser) {
        String out = "";

        if (givenUser.length() > 0){
            this.userAuthenticated = givenUser;
            messageProtocol.isValidAuthentication(true);
            out =  MessageProtocol.AUTHENTICATION_OK;
        } else {
            messageProtocol.isValidAuthentication(false);
            out =  "Error: Username is empty";
        }

        return out;
    }

    public Object readMessage() throws IOException, ClassNotFoundException {
        return this.objectInputStream.readObject();
    }

    public void sendMessage(Object toSend) throws IOException {
        try {
            objectOutputStream.writeObject(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close () {
        this.closeInput();
        this.closeOutput();
        this.closeSocket();
    }

    private void closeInput () {
        try {
            this.objectInputStream.close ();
        } catch (Exception e) {}
    }

    private void closeOutput () {
        try {
            this.objectOutputStream.close ();
        } catch (Exception e) {}
    }

    private void closeSocket () {
        try {
            this.clientSocket.close ();
        } catch (Exception e) {}
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

}

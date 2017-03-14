package tp1.ej03.Server;

import tp1.ej03.Message;
import tp1.ej03.MessageProtocol;

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
public class ServerThread implements Runnable{

    private Socket clientSocket;
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;

    private MessageProtocol messageProtocol;
    private String userAuthenticated;
    private HashMap<String, List<Message>> messages;

    public ServerThread(Socket clientSocket, HashMap<String, List<Message>> messages) {
        try {
            this.clientSocket = clientSocket;
            this.messages = messages;
            this.messageProtocol = new MessageProtocol();
            this.socketInput = new ObjectInputStream(clientSocket.getInputStream());
            this.socketOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            this.out("Error in instantiating new server thread");
            this.close();
        }
    }

    @Override
    public void run() {
        try {

            boolean socketClosed = this.clientSocket.isClosed();
            while (!socketClosed){
                Object objectFromClient = readFromSocket();
                if (objectFromClient == null) break;
                this.handleClientInput(objectFromClient);

                socketClosed = this.clientSocket.isClosed();
            }
            this.close();

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

    private void handleClientInput(Object objectFromClient) {
        Object objectToClient = null;
        int protocolState = this.messageProtocol.getState();

        if (protocolState == MessageProtocol.AUTHENTICATING){
            objectToClient = this.handleAuthentication(objectFromClient);
        } else if (protocolState == MessageProtocol.READY) {
            objectToClient = this.onClientRequest(objectFromClient);
        }

        if (objectToClient != null)
            this.sendToSocket(objectToClient);
    }

    private Object onClientRequest(Object request) {
        Object out = new Object();

        if (request instanceof Message) {
            this.addMessage((Message) request);
            out = MessageProtocol.MESSAGE_SENT_OK;
        }  else if (request.toString().equals(MessageProtocol.READ_MESSAGES)){
            out = this.readMessagesSentTo(this.userAuthenticated);
        }

        return out;
    }

    private List<Message> readMessagesSentTo(String userAuthenticated){
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
        String destination = message.getTo();

        messages.putIfAbsent(destination, new ArrayList<Message>());
        messages.get(destination).add(message);

        System.out.println("Message Added: \n - " + message);
    }

    private String handleAuthentication(Object objectFromClient) {

        if (!(objectFromClient instanceof String))
            return  "Error: Username datatype is not valid";

        String givenUser = objectFromClient.toString();

        if (givenUser.length() <= 0)
            return "Error: Username is empty";

        return authenticate(givenUser);
    }

    private String authenticate(String givenUser) {
        this.userAuthenticated = givenUser;
        messageProtocol.setState(MessageProtocol.READY);
        return  MessageProtocol.AUTHENTICATION_OK;
    }

    public Object readFromSocket() throws IOException, ClassNotFoundException {
        Object read = this.socketInput.readObject();
        out("Read from Client: " + read);
        return read;
    }

    public void sendToSocket(Object toSend) {
        try {
            out("Sending to Client: " + toSend);
            socketOutput.writeObject(toSend);
        } catch (IOException e) {
            out("IOException: Error in sending object to socket");
            this.close();
        }
    }

    public void close () {
        this.closeInput();
        this.closeOutput();
        this.closeSocket();
    }

    private void closeInput () {
        try {
            this.socketInput.close();
        } catch (Exception e) {}
    }

    private void closeOutput () {
        try {
            this.socketOutput.close();
        } catch (Exception e) {}
    }

    private void closeSocket () {
        try {
            this.clientSocket.close ();
        } catch (Exception e) {}
    }

    private void out(String toPrint){
        System.out.println(toPrint);
    }

}

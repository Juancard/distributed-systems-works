package tp1.ej3;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class MyServerThread implements Runnable{

    private final ArrayList<Message> messages;
    private long threadId;
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private MessageProtocol messageProtocol;

    private String userAuthenticated;

    public MyServerThread(Socket clientSocket, ArrayList<Message> messages) {
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
            Object protocolOutput = messageProtocol.processInput(null);
            this.sendMessage(protocolOutput);
            while (true){
                // It stays block until a new message arrive
                Object received = readMessage();
                if (received == null) break;
                System.out.println("Client: " + received);

                protocolOutput = messageProtocol.processInput(received);
                int protocolState = messageProtocol.getState();

                if (protocolState == MessageProtocol.AUTHENTICATING){
                    protocolOutput = this.authenticate((String) protocolOutput);
                }

                this.sendMessage(protocolOutput);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String authenticate(String givenUser) {
        String out = "";
        if (givenUser.length() > 0){
            this.userAuthenticated = givenUser;
            messageProtocol.isValidAuthentication(true);
            out =  "User has authenticated successfully";
        } else {
            messageProtocol.isValidAuthentication(false);
            out =  "Error: Username is empty";
        }

        return out;
    }

    public Object readMessage() throws IOException, ClassNotFoundException {
        try {
            return this.objectInputStream.readObject();
        } catch (EOFException e){
            return null;
        }
    }

    public void sendMessage(Object toSend) throws IOException {
        try {
            objectOutputStream.writeObject(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

}

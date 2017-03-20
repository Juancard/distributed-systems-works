package Tp1.Ex04.Client;

import Common.CommonMain;
import Tp1.Ex03.Message;

import java.util.List;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class ClientConsole {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5004;
    private static final int TP_NUMBER = 1;
    private static final int EXERCISE_NUMBER = 4;

    private static Scanner sc = new Scanner(System.in);
    private static String username;
    private static MessageClient myMessageClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "Message ServerWithSocket using Sockets");
        newClient();
        handleAuthentication();
        handleMainOptions();
        myMessageClient.close();
    }

    private static void newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        myMessageClient = new MessageClient(host, port);
    }

    private static void handleMainOptions() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
                CommonMain.createSection("Read Messages");
                handleReadMessages();
                CommonMain.pause();
            } else if (opcion.equals("2")){
                CommonMain.createSection("Send New Message");
                handleNewMessage();
                CommonMain.pause();
            }
        }
    }

    private static void showMain(){
        CommonMain.createSection("Message ServerWithSocket - Main");
        System.out.println("1 - Read Messages");
        System.out.println("2 - Send New Message");
        System.out.println("0 - Salir");
        System.out.println("");
        System.out.print("Ingrese opción: ");
    }

    private static void handleAuthentication() {
        String authenticationState;
        while (true) {
            username = askForAuthentication();
            authenticationState = myMessageClient.sendAuthenticationRequest(username);
            if (authenticationState == "") break;
            else System.out.println(authenticationState);
        }
        System.out.println("User has authenticated successfully");
    }

    private static String askForAuthentication() {
        System.out.print("Enter your username: ");
        return sc.nextLine();
    }

    private static void handleReadMessages() {
        List<Message> messagesReceived = myMessageClient.sendReadMessagesRequest();
        int totalMessages = messagesReceived.size();
        showNumberOfMessagesReceived(totalMessages);
        for (int i=0; i < totalMessages; i++){
            showReceivedMessage(messagesReceived.get(i));
            if (i < totalMessages - 1) CommonMain.pause();
        }
    }

    private static void showNumberOfMessagesReceived(int numberMessagesReceived) {
        if (numberMessagesReceived == 0) {
            System.out.println("You have received no messages yet");
        } else {
            System.out.printf("You have received %d messages\n", numberMessagesReceived);
        }
    }

    private static void showReceivedMessage(Message m) {
        int minSeparatorSize = 10;
        String separator = CommonMain.repeat(minSeparatorSize + m.getBody().length(), "=");
        System.out.println(separator);
        System.out.println("From: " + m.getFrom());
        System.out.println("To: " + m.getTo());
        System.out.println(separator);
        System.out.println(m.getBody());
        System.out.println(separator);
    }

    private static void handleNewMessage() {
        Message message = askForMessage();
        boolean isMessageSent = myMessageClient.sendNewMessageRequest(message);
        showMessageSentState(isMessageSent);
    }

    private static Message askForMessage() {
        String destination = getMessageDestination();
        String messageBody = getMessageBody();
        return new Message(messageBody, username, destination);
    }

    private static String getMessageDestination() {
        System.out.print("Who are you writing to?: ");
        return sc.nextLine();
    }

    private static String getMessageBody() {
        System.out.println("Write your message (Enter to send): ");
        return sc.nextLine();
     }

    private static void showMessageSentState(boolean isMessageSent) {
        if (isMessageSent){
            System.out.println("Message was sent successfully");
        } else {
            System.out.println("Error in sending message. Try again later.");
        }
    }

}

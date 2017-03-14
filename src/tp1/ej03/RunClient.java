package tp1.ej03;

import java.util.List;
import java.util.Iterator;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class RunClient {

    private static Scanner sc = new Scanner(System.in);
    private static String username;
    private static MyClient myClient;

    public static void main(String[] args) {
        newClient();
        showWelcomeMessage();
        handleAuthentication();
        handleMainOptions();
        myClient.close();
    }

    private static void newClient() {
        String host = "localhost";
        int port = 5003;
        myClient = new MyClient(host, port);
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
                createSection("Read Messages");
                handleReadMessages();
                pause();
            } else if (opcion.equals("2")){
                createSection("Send New Message");
                handleNewMessage();
                pause();
            }
        }
    }

    private static void showMain(){
        createSection("Message Server - Main");
        System.out.println("1 - Read Messages");
        System.out.println("2 - Send New Message");
        System.out.println("0 - Salir");
        System.out.println("");
        System.out.print("Ingrese opción: ");
    }

    private static void handleAuthentication() {
        String authenticationState;
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            username = askForAuthentication();
            myClient.sendToSocket(username);

            authenticationState = myClient.readFromSocket().toString();
            isAuthenticated = checkAuthentication(authenticationState);
            if (!isAuthenticated)
                System.out.println(authenticationState);
        }
        System.out.println("User has authenticated successfully");
    }

    private static boolean checkAuthentication(String authenticationState) {
        return authenticationState.equals(MessageProtocol.AUTHENTICATION_OK);
    }

    private static String askForAuthentication() {
        System.out.print("Enter your username: ");
        return sc.nextLine();
    }

    private static void handleReadMessages() {
        List<Message> messagesReceived = getMessagesReceived();
        int totalMessages = messagesReceived.size();
        showNumberOfMessagesReceived(totalMessages);
        for (int i=0; i < totalMessages; i++){
            showReceivedMessage(messagesReceived.get(i));
            if (i < totalMessages - 1) pause();
        }
    }

    private static List<Message> getMessagesReceived() {
        myClient.sendToSocket(MessageProtocol.READ_MESSAGES_RECEIVED);
        return (List<Message>) myClient.readFromSocket();
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
        String separator = repeat(minSeparatorSize + m.getBody().length(), "=");
        System.out.println(separator);
        System.out.println("From: " + m.getFrom());
        System.out.println("To: " + m.getTo());
        System.out.println(separator);
        System.out.println(m.getBody());
        System.out.println(separator);
    }

    private static void handleNewMessage() {
        handleNewMessageRequest();
        handleNewMessageResponse();
    }

    private static void handleNewMessageRequest() {
        Message message = askForMessage();
        myClient.sendToSocket(message);
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

    private static void handleNewMessageResponse() {
        Object response = myClient.readFromSocket();
        if (isMessageSent(response)){
            System.out.println("Message was sent successfully");
        } else {
            System.out.println("Error in sending message. Try again later.");
        }
    }

    private static boolean isMessageSent(Object response) {
        return response.equals(MessageProtocol.MESSAGE_SENT_OK);
    }

    private static void showWelcomeMessage() {
        String title = "Sistemas Distribuidos y Programación Paralela";
        String subtitle = "TP N°1 - Ej3 - Message Server";
        createSection(title + "\n" + subtitle);
    }

    private static void createSection(String section){
        String separatorChar = "*";
        int separatorMinLength = 6;
        int longestString = -1;

        for (String s : section.split("\n"))
            if (s.length() > longestString) longestString = s.length();

        String separator = repeat(separatorMinLength + longestString, separatorChar);
        showSection(separator, section);
    }

    private static void showSection(String separator, String section) {
        System.out.println("");
        System.out.println(separator);
        System.out.println(section);
        System.out.println(separator);
        System.out.println("");
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private static void pause() {
        System.out.println("\nPresione cualquier tecla para continuar");
        sc.nextLine();
    }
}

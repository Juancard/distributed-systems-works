package tp1.ej03;

import java.util.List;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class ej3Main {

    private static Scanner sc = new Scanner(System.in);
    private static String username;
    private static MyClient myClient;

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5003;

        showWelcomeMessage();
        myClient = new MyClient(host, port);
        MessageProtocol messageProtocol = new MessageProtocol();

        authenticate();

        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
                showSection("Read Messages");
                handleReadMessages();
                pause();
            } else if (opcion.equals("2")){
                showSection("Send New Message");
                handleNewMessage();
                pause();
            }
        }
        myClient.close();

    }

    private static void authenticate() {
        String authenticationState;
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            System.out.print("Enter your username: ");
            username = sc.nextLine();
            myClient.sendToSocket(username);

            authenticationState = myClient.readFromSocket().toString();
            isAuthenticated = authenticationState.equals(MessageProtocol.AUTHENTICATION_OK);

            if (!isAuthenticated)
                System.out.println(authenticationState);
        }
        System.out.println("User has authenticated successfully");
    }

    private static void handleReadMessages() {
        myClient.sendToSocket(MessageProtocol.READ_MESSAGES_RECEIVED);
        List<Message> messagesReceived = (List<Message>) myClient.readFromSocket();

        int numberOfMessagesReceived = messagesReceived.size();
        if (numberOfMessagesReceived == 0) {
            System.out.println("You have received no messages yet");
        } else {
            System.out.printf("You have received %d messages\n", numberOfMessagesReceived);

            for (Message m : messagesReceived) {
                System.out.println("");
                System.out.println("From: " + m.getFrom());
                System.out.println("To: " + m.getTo());
                System.out.println(m.getBody());
                System.out.println("");
            }
        }

    }

    private static void handleNewMessage() {
        String destination = getMessageDestination();
        String messageBody = getMessageBody();
        Message message = new Message(messageBody, username, destination);
        myClient.sendToSocket(message);
        if (myClient.readFromSocket().equals(MessageProtocol.MESSAGE_SENT_OK)){
            System.out.println("Message was sent successfully");
        } else {
            System.out.println("Error in sending message. Try again later.");
        }
    }

    private static String getMessageDestination() {
        System.out.print("Who are you writing to?: ");
        return sc.nextLine();
    }

    private static String getMessageBody() {
        System.out.println("Write your message (Enter to send): ");
        return sc.nextLine();
     }

    private static void showWelcomeMessage() {
        String title = "Sistemas Distribuidos y Programación Paralela";
        String subtitle = "TP N°1 - Ej3 - Message Server";
        showSection(title + "\n" + subtitle);
    }

    private static void showSection(String section){
        int longestString = -1;
        for (String s : section.split("\n"))
            if (s.length() > longestString) longestString = s.length();

        String separatorChar = "*";
        int separatorMinLength = 6;
        String separator = separatorChar  + repeat(separatorMinLength + longestString, separatorChar);

        System.out.println("");
        System.out.println(separator);
        System.out.println(section);
        System.out.println(separator);
        System.out.println("");
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private static void showMain(){
        showSection("Message Server - Main");
        System.out.println("1 - Read Messages");
        System.out.println("2 - Send New Message");
        System.out.println("");
        System.out.println("0 - Salir");
        System.out.println("");
        System.out.print("Ingrese opción: ");
    }

    private static void pause() {
        System.out.println("\nPresione cualquier tecla para continuar");
        sc.nextLine();
    }
}

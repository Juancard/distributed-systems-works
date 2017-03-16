package tp1.ej03.Client;

import tp1.ej03.Client.MessageClient;
import tp1.ej03.Message;

import java.util.List;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class ClientConsole {

    private static Scanner sc = new Scanner(System.in);
    private static String username;
    private static MessageClient myMessageClient;

    public static void main(String[] args) {
        newClient();
        showWelcomeMessage();
        handleAuthentication();
        handleMainOptions();
        myMessageClient.close();
    }


    private static void newClient() {
        String host = askForIp();
        int port = askForPort();
        myMessageClient = new MessageClient(host, port);
    }
    
    private static String askForIp(){
    	final String DEFAULT_IP = "localhost";
    	System.out.printf("Enter server Ip [%s]: ", DEFAULT_IP);
    	String ip = sc.nextLine();
    	if (ip.length() == 0)
    		ip = DEFAULT_IP;
    	return ip;
    }
    
    private static int askForPort(){
    	final int DEFAULT_PORT = 5004;
    	
    	System.out.printf("Enter server Port [%s]: ", DEFAULT_PORT);
    	String givenPort = sc.nextLine();
    	int port;
    	if (givenPort.length() == 0)
    		port = DEFAULT_PORT;
    	else
	    	try {
	    		port = Integer.parseInt(givenPort);
	    	} catch(NumberFormatException e){
	    		System.out.println("Not a valid port number. Default port has been set.");
	    		port = DEFAULT_PORT;
	    	}
    	
    	return port;
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
            if (i < totalMessages - 1) pause();
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
        String separator = repeat(minSeparatorSize + m.getBody().length(), "=");
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

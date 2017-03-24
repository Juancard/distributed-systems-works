package Tp1.Ex07.Client;

import Common.CommonMain;
import Tp1.Ex03.Common.Message;
import Tp1.Ex07.Common.NotValidUserException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:17
 */
public class ConsoleClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5007;
    private static final int EXERCISE_NUMBER = 7;
    private static final int TP_NUMBER = 1;

    private static Scanner scanner = new Scanner(System.in);
    private static MessageClient messageClient;
    private static String username;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "RMI Message Service");
        try {
            messageClient = newClient();
        } catch (Exception e) {
            CommonMain.display("Error starting RMI client: " + e.toString());
            System.exit(1);
        }
        handleAuthentication();
        handleMainOptions();
    }

    private static MessageClient newClient() throws RemoteException, NotBoundException {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        return new MessageClient(host, port);
    }

    private static void handleAuthentication(){
        CommonMain.createSection("Authentication");
        boolean isAuthenticated = false;

        while (!isAuthenticated) {
            username = Tp1.Ex03.Client.ClientConsole.askForAuthentication();

            try {
                isAuthenticated = messageClient.authenticate(username);
            } catch (RemoteException e) {
                CommonMain.display("Error in server while authenticating user: " + e.getMessage());
                System.exit(1);
            } catch (NotValidUserException e) {
                CommonMain.display("Not a valid user: " + e.getMessage());
            }

        }

        CommonMain.display("User has authenticated successfully");
    }

    private static void handleMainOptions() {
        String option;
        boolean exit = false;

        while (!exit) {
            Tp1.Ex03.Client.ClientConsole.showMain();
            option = scanner.nextLine();
            if (option.equals("0")) {
                exit = true;
            }  else if (option.equals("1")){
                CommonMain.createSection("Read Messages");
                handleReadMessages();
                CommonMain.pause();
            } else if (option.equals("2")){
                CommonMain.createSection("New Message");
                handleSendNewMessage();
                CommonMain.pause();
            }
        }
    }

    private static void handleReadMessages() {
        List<Message> messagesReceived = null;
        try {
            messagesReceived = messageClient.readMessagesSentTo(username);
            Tp1.Ex03.Client.ClientConsole.handleMessagesDisplay(messagesReceived);
        } catch (RemoteException e) {
            CommonMain.display("Error in server: " + e.getMessage());
        }
    }

    private static void handleSendNewMessage() {
        String to = Tp1.Ex03.Client.ClientConsole.getMessageDestination();
        String body = Tp1.Ex03.Client.ClientConsole.getMessageBody();
        try {
            messageClient.sendNewMessage(body, username, to);
            CommonMain.display("Message added successfully");
        } catch (RemoteException e) {
            CommonMain.display("Error in server: " + e.getMessage());
        }
    }

}

package Tp1.Ex02.Client;

import Common.CommonMain;

import java.io.IOException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 16:35
 */
public class ConsoleClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5002;
    private static final int TP_NUMBER = 1;
    private static final int EXERCISE_NUMBER = 2;

    private static ClientWithSocket clientWithSocket;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "Using Sockets with Threads");
        clientWithSocket = newClient();
        handleCommunication();
        clientWithSocket.close();
    }

    private static ClientWithSocket newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        return new ClientWithSocket(host, port);
    }

    private static void handleCommunication() {
        String toSend;
        String exitWith = "-1";
        System.out.printf("\nSend a Message to the server (%s to exit): \n", exitWith);
        while (true){
            System.out.print("Me: ");
            toSend = scanner.nextLine();
            if (toSend.equals(exitWith)) break;
            try {
                clientWithSocket.sendMessage(toSend);
                String received = clientWithSocket.readMessage();

                if (received == null) {
                    System.out.println("Server has shutted down");
                    break;
                }

                System.out.println("Server: " + received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

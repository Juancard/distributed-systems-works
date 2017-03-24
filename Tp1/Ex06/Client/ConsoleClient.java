package Tp1.Ex06.Client;

import Common.CommonMain;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:17
 */
public class ConsoleClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 6006;
    private static final int EXERCISE_NUMBER = 6;
    private static final int TP_NUMBER = 1;

    private static Scanner sc = new Scanner(System.in);
    private static VectorClient vectorClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "RMI Vector Service");
        try {
            vectorClient = newClient();
        } catch (RemoteException e) {
            CommonMain.display(e.toString());
        } catch (NotBoundException e) {
            CommonMain.display(e.toString());
        }
    }

    private static VectorClient newClient() throws RemoteException, NotBoundException {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        return new VectorClient(host, port);
    }



}

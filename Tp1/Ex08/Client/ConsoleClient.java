package Tp1.Ex08.Client;

import Common.CommonMain;
import Tp1.Ex08.Common.ITask;

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
    private static final int DEFAULT_PORT = 5008;
    private static final int EXERCISE_NUMBER = 8;
    private static final int TP_NUMBER = 1;

    private static Scanner scanner = new Scanner(System.in);
    private static TaskClient taskClient;
    private static PrimeCalculator primeCalculator;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "RMI Message Service");
        taskClient = newClient();
        primeCalculator = new PrimeCalculator();
        handleMain();
    }

    private static TaskClient newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);

        try {
            return new TaskClient(host, port);
        } catch (RemoteException e) {
            CommonMain.display("Error starting RMI client: " + e.toString());
            System.exit(1);
            return null;
        } catch (NotBoundException e) {
            CommonMain.display("Error starting RMI client: " + e.toString());
            System.exit(1);
            return null;
        }
    }

    private static void handleMain() {
        String option;
        boolean exit = false;

        while (!exit) {
            showMain();
            option = scanner.nextLine();
            if (option.equals("-1")) {
                exit = true;
            } else if (option.equals("1")){
                CommonMain.createSection("Prime Calculator");
                handlePrimeCalculator();
                CommonMain.pause();
            }
        }
    }

    private static void handlePrimeCalculator() {
        System.out.print("I want primes until this value: ");
        String givenValue = scanner.nextLine();
        try {
            primeCalculator.setUntilThisValue(Integer.parseInt(givenValue));
            List<Integer> primes = (List<Integer>) taskClient.executeInServer(primeCalculator);
            CommonMain.display("Primes are: " + primes.toString());
        } catch (NumberFormatException e) {
            CommonMain.display("Error: not a valid number");
        } catch (RemoteException e) {
            CommonMain.display("Error in server: " + e.getMessage());
        }
    }

    private static void showMain(){
        CommonMain.createSection("Task Service - Main");
        CommonMain.display("1 - Prime Calculator");
        CommonMain.display("-1 - Exit");
        CommonMain.display("");
        System.out.print("Select option: ");
    }

    private static String listToString(List<Integer> values) {
        String out = "[";
        for (int i=0; i<values.size(); i++) {
            out += values.get(i);
            out += (i != values.size() - 1)? ", " : "]";
        }
        return out;
    }

}

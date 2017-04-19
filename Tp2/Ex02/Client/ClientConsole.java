package Tp2.Ex02.Client;

import Common.CommonMain;

import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class ClientConsole {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5022;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String EXERCISE_TITLE = "Bank Server";

    private static Scanner sc = new Scanner(System.in);
    private static AccountClient myAccountClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        newClient();
        handleAuthentication();
        handleMainOptions();
    }

    private static void handleAuthentication() {
        CommonMain.createSection("Authentication");
        String authenticationState;
        while (true) {
            authenticationState = myAccountClient.authenticate(askForAuthentication());
            if (authenticationState == "") break;
            else System.out.println(authenticationState);
        }
        System.out.println("User has authenticated successfully");
    }

    public static String askForAuthentication() {
        System.out.print("Enter your username: ");
        return sc.nextLine();
    }


    private static void newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        myAccountClient = new AccountClient(host, port);
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
                CommonMain.createSection("Deposit");
                handleDeposit();
                CommonMain.pause();
            } else if (opcion.equals("2")){
                CommonMain.createSection("Extract");
                handleExtraction();
                CommonMain.pause();
            }
        }
        myAccountClient.close();
    }

    private static void handleDeposit() {
       try {
            double amount = Double.parseDouble(askForAmount());
            double resultingAccountBalance = myAccountClient.deposit(amount);
            CommonMain.display("\nDeposit was successful");
            CommonMain.display("Your current account balance is: " + resultingAccountBalance);
        } catch(NumberFormatException e){
            CommonMain.display("\nError: Not a valid amount to deposit");
        } catch (Exception e) {
            CommonMain.display("\nDeposit failed. " + e.getMessage());
        }
    }

    private static void handleExtraction() {
        try {
            double amount = Double.parseDouble(askForAmount());
            double resultingAccountBalance = myAccountClient.extract(amount);
            CommonMain.display("\nExtraction was successful");
            CommonMain.display("Your current account balance is: " + resultingAccountBalance);
        } catch(NumberFormatException e){
            CommonMain.display("\nError: Not a valid amount to extract");
        } catch (Exception e) {
            CommonMain.display("\nExtract failed. " + e.getMessage());
        }
    }

    private static String askForAmount() {
        System.out.print("Enter Amount: ");
        return sc.nextLine();
    }

    public static void showMain(){
        CommonMain.createSection(EXERCISE_TITLE + " - Main");
        System.out.println("1 - Deposit");
        System.out.println("2 - Extract");
        System.out.println("0 - Exit");
        System.out.println("");
        System.out.print("Enter option: ");
    }

}

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
    private static final int DEFAULT_PORT_DEPOSIT = 5122;
    private static final int DEFAULT_PORT_EXTRACT = 5222;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String EXERCISE_TITLE = "Bank Server";

    private static Scanner sc = new Scanner(System.in);
    private static AccountClient myAccountClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        newClient();
        handleMainOptions();
    }

    private static void newClient() {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int portToDeposit = CommonMain.askForPort("Enter deposit port", DEFAULT_PORT_DEPOSIT);
        int portToExtract = CommonMain.askForPort("Enter extraction port", DEFAULT_PORT_EXTRACT);
        myAccountClient = new AccountClient(host, portToDeposit, portToExtract);
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
    }

    private static void handleDeposit() {
       try {
            String owner = askForOwner();
            double amount = Double.parseDouble(askForAmount());
            double resultingAccountBalance = myAccountClient.deposit(owner, amount);
            CommonMain.display("\nDeposit was successful");
            CommonMain.display("New account balance is: " + resultingAccountBalance);
        } catch(NumberFormatException e){
            CommonMain.display("\nError: Not a valid amount to deposit");
        } catch (Exception e) {
            CommonMain.display("\nDeposit failed. ");
            if (e.getMessage() != null) CommonMain.display("Reason: " + e.getMessage());
        }
    }

    private static void handleExtraction() {
        try {
            String owner = askForOwner();
            double amount = Double.parseDouble(askForAmount());
            double resultingAccountBalance = myAccountClient.extract(owner, amount);
            CommonMain.display("\nExtraction was successful");
            CommonMain.display("New account balance is: " + resultingAccountBalance);
        } catch(NumberFormatException e){
            CommonMain.display("\nError: Not a valid amount to extract");
        } catch (Exception e) {
            CommonMain.display("\nDeposit failed. ");
            if (e.getMessage() != null) CommonMain.display("Reason: " + e.getMessage());
        }
    }

    private static String askForOwner() {
        System.out.print("Enter Account owner: ");
        return sc.nextLine();
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

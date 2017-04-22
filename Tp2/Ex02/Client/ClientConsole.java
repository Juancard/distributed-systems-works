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

    private Scanner sc = new Scanner(System.in);
    private AccountClient myAccountClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE + " - Client side");
        ClientConsole clientConsole = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT);
    }

    public ClientConsole(String defaultHost, int defaultPortDeposit, int defaultPortExtract){
        newClient(defaultHost, defaultPortDeposit, defaultPortExtract);
        handleMainOptions();
    }

    private void newClient(String defaultHost, int defaultPortDeposit, int defaultPortExtract) {
        String host = CommonMain.askForHost(defaultHost);
        int portToDeposit = CommonMain.askForPort("Enter deposit port", defaultPortDeposit);
        int portToExtract = CommonMain.askForPort("Enter extraction port", defaultPortExtract);
        myAccountClient = new AccountClient(host, portToDeposit, portToExtract);
    }

    private void handleMainOptions() {
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

    private void handleDeposit() {
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

    private void handleExtraction() {
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

    private String askForOwner() {
        System.out.print("Enter Account owner: ");
        return sc.nextLine();
    }

    private String askForAmount() {
        System.out.print("Enter Amount: ");
        return sc.nextLine();
    }

    public void showMain(){
        CommonMain.createSection(EXERCISE_TITLE + " - Main");
        System.out.println("1 - Deposit");
        System.out.println("2 - Extract");
        System.out.println("0 - Exit");
        System.out.println("");
        System.out.print("Enter option: ");
    }

}

package Tp2.Ex02.Client;

import Common.CommonMain;
import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 */
public class RunBankClient {
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex02/config.properties";

    private Scanner sc = new Scanner(System.in);
    private AccountClient myAccountClient;

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);

        String serverHost = properties.getProperty("SERVER_HOST");
        int depositPort = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int extractPort = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));
        RunBankClient runBankClient = new RunBankClient(serverHost, depositPort, extractPort);
    }

    public RunBankClient(String serverHost, int portDeposit, int portExtract){
        myAccountClient = new AccountClient(serverHost, portDeposit, portExtract);
        handleMainOptions();
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
            CommonMain.display("\nExtraction failed. ");
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
        CommonMain.createSection("Bank app - Main");
        System.out.println("1 - Deposit");
        System.out.println("2 - Extract");
        System.out.println("0 - Exit");
        System.out.println("");
        System.out.print("Enter option: ");
    }

}

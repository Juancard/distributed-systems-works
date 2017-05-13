package Tp2.Ex02.Client;

import Common.CommonMain;
import Common.PropertiesManager;

import java.util.Properties;

/**
 * User: juan
 * Date: 21/04/17
 * Time: 13:51
 */

/**
 *  To Solve this exercise: "Forzar, y mostrar como se logra, errores en el acceso al uso compartido"
 */
public class TestSync {
    private static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex02/config.properties";

    public static void main(String[] args) throws Exception {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);

        // Server data
        String serverHost = properties.getProperty("SERVER_HOST");
        int depositPort = Integer.parseInt(properties.getProperty("SERVER_PORT_DEPOSIT"));
        int extractPort = Integer.parseInt(properties.getProperty("SERVER_PORT_EXTRACT"));

        final AccountClient client1 = newClient(serverHost, depositPort, extractPort);
        final AccountClient client2 = newClient(serverHost, depositPort, extractPort);
        final String accountToDepositIn = "juan";
        final double startingBalance = client1.deposit("juan", 0);
        final double firstDepositAmount = 10.0;
        final double secondDepositAmount = 20.0;
        final double finalBalanceShouldBe =  startingBalance + firstDepositAmount + secondDepositAmount;

        CommonMain.display("We will perform two deposits in same account and see what's the new balance in that account");
        CommonMain.display("");
        CommonMain.display("Deposits will be performed on account: " + accountToDepositIn);
        CommonMain.display("");
        CommonMain.display("First, let's check the current balance");
        CommonMain.display("Current balance: " + startingBalance);
        CommonMain.display("");
        CommonMain.display("Now we start");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                CommonMain.display("Deposit 1. Calls deposit with amount: " + firstDepositAmount);
                try {
                    double balance = client1.deposit("juan", firstDepositAmount);
                    CommonMain.display("Deposit 1. Resulting balance: " + Double.toString(balance));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                CommonMain.display("Deposit 2. Calls deposit with amount: " + secondDepositAmount);
                try {
                    double balance = client2.deposit("juan", secondDepositAmount);
                    CommonMain.display("Deposit 2. Resulting balance: " + Double.toString(balance));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        double finalBalanceIs = client1.deposit(accountToDepositIn, 0);
        CommonMain.display("Final balance is: " + finalBalanceIs);
        CommonMain.display("Final balance should be: " + finalBalanceShouldBe);
    }

    private static AccountClient newClient(String serverHost, int depositPort, int extractPort) {
        return new AccountClient(serverHost, depositPort, extractPort);
    }
}

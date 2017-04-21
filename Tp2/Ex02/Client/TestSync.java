package Tp2.Ex02.Client;

import Common.CommonMain;

/**
 * User: juan
 * Date: 21/04/17
 * Time: 13:51
 */

/**
 *  To Solve this: "Forzar, y mostrar como se logra, errores en el acceso al uso compartido"
 */
public class TestSync {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT_DEPOSIT = 5122;
    private static final int DEFAULT_PORT_EXTRACT = 5222;
    private static final int TP_NUMBER = 2;
    private static final int EXERCISE_NUMBER = 2;
    private static final String EXERCISE_TITLE = "Bank Server - Force to Synchronization Errors";

    public static void main(String[] args) throws Exception {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, EXERCISE_TITLE);

        final AccountClient client1 = newClient();
        final AccountClient client2 = newClient();
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

    private static AccountClient newClient() {
        return new AccountClient(DEFAULT_HOST, DEFAULT_PORT_DEPOSIT, DEFAULT_PORT_EXTRACT);
    }
}

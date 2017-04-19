package Tp2.Ex02.Server;

import Tp2.Ex02.Common.BankException;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 13:09
 */
public class BankAccount {
    private String owner;
    private double balance;

    public BankAccount(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public static BankAccount openNew(String username) {
        return new BankAccount(username, 0);
    }

    public double deposit(double amount) throws BankException {
        if (amount < 0) throw new BankException("Amount can not be a negative value");
        if (amount > 0) this.balance += amount;
        return this.balance;
    }

    public double extract(double amount) throws BankException {
        if (amount < 0) throw new BankException("Amount can not be a negative value");
        if (amount > this.balance) throw new BankException("Not enough money in account");
        this.balance -= amount;
        return this.balance;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }
}

package Tp1.Ex08.Client;

import Tp1.Ex08.Common.ITask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: juan
 * Date: 24/03/17
 * Time: 21:40
 */
public class PrimeCalculator implements ITask, Serializable{

    private int untilThisValue;

    public PrimeCalculator(int untilThisValue) {
        this.untilThisValue = untilThisValue;
    }

    public PrimeCalculator() {}

    @Override
    public Object execute() {
        List<Integer> primes = new ArrayList<Integer>();

        if (this.untilThisValue < 2) return primes;
        for (int i = 2; i < this.untilThisValue; i++)
            if (this.isPrime(i, i - 1))
                primes.add(i);

        return primes;
    }

    public boolean isPrime(int x, int i) {
        if (i == 1) return true;
        if (x % i == 0) return false;
        return isPrime(x, i - 1);
    }

    public void setUntilThisValue(int untilThisValue) {
        this.untilThisValue = untilThisValue;
    }

}

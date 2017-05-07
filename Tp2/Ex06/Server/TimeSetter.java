package Tp2.Ex06.Server;

import Tp2.Ex06.Common.SharedDate;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:05
 */
public class TimeSetter implements Runnable{
    private SharedDate date;
    private int updatePeriod;

    public TimeSetter (SharedDate date, int updatePeriod) {
        this.date = date;
        this.updatePeriod = updatePeriod;
    }

    @Override
    public void run() {
        while (true) {
            this.date.setCurrentDate();
            try {
                Thread.sleep(this.updatePeriod);
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}

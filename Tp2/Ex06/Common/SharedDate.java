package Tp2.Ex06.Common;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 13:17
 */
public class SharedDate {

    private Date date;
    private boolean hasChanged;
    private int totalConsumers;
    private ArrayList<Integer> consumersThatHaveReadDate;


    public SharedDate() {
        this.totalConsumers = 0;
        this.consumersThatHaveReadDate = new ArrayList<Integer>();
        this.hasChanged = false;
    }

    public void addConsumer(){
        this.totalConsumers++;
    }

    public void removeConsumer(){
        this.totalConsumers--;
    }

    public synchronized void setCurrentDate() {
        Date date = new Date();
        this.setDate(date);
    }

    public synchronized void setDate(Date date) {
        this.date = date;
        this.consumersThatHaveReadDate = new ArrayList<Integer>();
        this.hasChanged = true;
        System.out.println("Date updated. It's: " + this.date);
        notifyAll();
    }

    public synchronized Date getCurrentDate() {
        while (!this.hasChanged)
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}

        if (this.consumersThatHaveReadDate.size() >= totalConsumers){
            this.consumersThatHaveReadDate = new ArrayList<Integer>();
            this.hasChanged = false;
        } else {
            int consumer = this.getCurrentThreadId();
            if (!(this.consumersThatHaveReadDate.contains(consumer)))
                this.consumersThatHaveReadDate.add(consumer);
        }

        return this.date;
    }

    public int getCurrentThreadId(){
        return (int) Thread.currentThread().getId();
    }
}

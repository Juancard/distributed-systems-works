package Tp2.Ex06.Server;

import Common.Socket.MyCustomWorker;
import Tp2.Ex06.Common.SharedDate;

import java.net.Socket;
import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 12:54
 */
public class TimeWorker extends MyCustomWorker {

    private SharedDate sharedDate;

    public TimeWorker(Socket clientSocket, SharedDate sharedDate) {
        super(clientSocket);
        this.sharedDate = sharedDate;
        this.sharedDate.addConsumer();
    }

    public void run() {
        try {
            boolean clientClosed = this.clientConnection.isClosed();
            while (!clientClosed){
                Date date = this.sharedDate.getCurrentDate();
                this.display(date.toString());
                this.sendToClient(date);
                clientClosed = this.clientConnection.isClosed();
            }
        } catch (Exception e) {
            this.display(e.getMessage());
        } finally {
            this.close();
        }
    }

    public void close(){
        this.sharedDate.removeConsumer();
        super.close();
    }
}

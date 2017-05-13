package Tp2.Ex06.Server;

import Common.Socket.MyCustomServer;
import Tp2.Ex06.Common.SharedDate;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 12:49
 */
public class TimeServer extends MyCustomServer implements Runnable{

    private SharedDate sharedDate;
    private TimeSetter timeSetter;
    private final int updatePeriod;

    public TimeServer(int port, int updatePeriod) {
        super(port);
        this.sharedDate = new SharedDate();
        this.updatePeriod = updatePeriod;
        this.timeSetter = new TimeSetter(sharedDate, updatePeriod);
    }

    protected Runnable newRunnable(Socket clientSocket){
        return new TimeWorker(clientSocket, this.sharedDate);
    }

    public void startServer() throws IOException {
        this.startTimeSetter();
        super.startServer();
    }

    private void startTimeSetter() {
        new Thread(this.timeSetter).start();
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

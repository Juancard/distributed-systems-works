package Tp2.Ex06.Client;

import Common.Socket.SocketConnection;

import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:46
 */
public class TimeClient extends SocketConnection implements Runnable{

    public TimeClient(String host, int port) {
        super(host, port);
    }

    public Date getCurrentDate() throws Exception {
        Object response = this.read();

        if (response instanceof Exception)
            throw (Exception) response;

        return (Date) this.read();
    }


    @Override
    public void run() {
        while (true) {
            try {
                this.out(this.getCurrentDate().toString());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

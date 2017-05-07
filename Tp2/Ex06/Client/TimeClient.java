package Tp2.Ex06.Client;

import Common.Socket.SocketConnection;

import java.io.IOException;
import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:46
 */
public class TimeClient extends SocketConnection{

    private Date clientDate;

    public TimeClient(String host, int port) {
        super(host, port);

    }

    public void startTimeListener() throws Exception {
        while (true) {
            Object response = this.read();

            if (response instanceof Exception)
                throw (Exception) response;

            this.clientDate = (Date) response;
            this.out(clientDate.toString());
        }
    }
}

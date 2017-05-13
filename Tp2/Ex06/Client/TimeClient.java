package Tp2.Ex06.Client;

import Common.Socket.SocketConnection;

import java.io.EOFException;
import java.io.IOException;
import java.util.Date;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 14:46
 */
public class TimeClient extends SocketConnection{

    private Date clientDate;

    public TimeClient(String host, int port) throws IOException {
        super(host, port);
    }

    public void startTimeListener() throws Exception {
        while (!(this.isClosed())) {
            Object response = this.read();

            if (response instanceof EOFException)
                throw (EOFException) response;
            if (response instanceof Exception)
                throw (Exception) response;

            this.clientDate = (Date) response;
            this.out(clientDate.toString());
        }
    }
}

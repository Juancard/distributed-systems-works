package Tp2.Ex06.Server;

import Common.Socket.MyCustomWorker;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * User: juan
 * Date: 07/05/17
 * Time: 12:54
 */
public class TimeWorker extends MyCustomWorker {
    public TimeWorker(Socket clientSocket) {
        super(clientSocket);
    }

    public void run() {
        try {

            boolean clientClosed = this.clientConnection.isClosed();
            while (!clientClosed){
                Object objectFromClient = this.clientConnection.read();
                if (objectFromClient == null) break;
                this.handleClientInput(objectFromClient);

                clientClosed = this.clientConnection.isClosed();
            }

        } catch (SocketException e) {
            this.display("Connection lost with client");
        } catch (EOFException e) {
            this.display("Client disconnected");
        } catch (IOException e) {
            this.display(e.getMessage());
        } catch (Exception e) {
            this.display(e.getMessage());
        } finally {
            this.close();
        }
    }
}

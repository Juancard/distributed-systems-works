package Tp2.Ex07.Server.LoadBalancer;

import Common.Socket.SocketConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 14:56
 */
public class Router {

    public static void route(Socket client, Socket server){
        SocketConnection clientConnection = new SocketConnection(client);
        SocketConnection serverConnection = new SocketConnection(server);

        Thread fromClientToServer = readAndSendThread(clientConnection, serverConnection);
        Thread fromServerToClient = readAndSendThread(serverConnection, clientConnection);

        fromClientToServer.start();
        fromServerToClient.start();

    }

    private static Thread readAndSendThread(final SocketConnection toReadFrom, final SocketConnection toSendTo){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                readAndSend(toReadFrom, toSendTo);
            }
        });
    }

    private static void readAndSend(SocketConnection toReadFrom, SocketConnection toSendTo){
        while (!(toReadFrom.isClosed()) && !(toSendTo.isClosed())){
            try {
                Object read = toReadFrom.read();
                toSendTo.send(read);
            } catch (IOException e) {
                toReadFrom.close();
                toSendTo.close();
                break;
            } catch (ClassNotFoundException e) {
                toReadFrom.close();
                toSendTo.close();
                break;
            }
        }
    }
}

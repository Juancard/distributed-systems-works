package Tp2.Ex07.Server.LoadBalancer;

import Common.ServerInfo;
import Common.Socket.MyCustomServer;
import Common.Socket.SocketConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 14:23
 */
public class LoadBalancer extends MyCustomServer implements Runnable{

    private ArrayList<ServerInfo> servers;
    private int serverIndex;

    public LoadBalancer(int port) {
        super(port);
        this.servers = new ArrayList<ServerInfo>();
        this.serverIndex = -1;
    }

    public void addServer(ServerInfo serverInfo) {
        this.servers.add(serverInfo);
        this.out("Load balancer adds server: " + serverInfo);
    }

    public void removeServer(ServerInfo serverInfo){
        this.servers.remove(serverInfo);
        this.out("Removed server: " + serverInfo);
    }

    public int nextServerIndex(){
        if (this.serverIndex < 0 || this.serverIndex >= this.servers.size() - 1)
            this.serverIndex = 0;
        else
            this.serverIndex++;
        return this.serverIndex;
    }

    protected Runnable newRunnable(Socket clientSocket) {
        ServerInfo serverToConnectTo = this.servers.get(this.nextServerIndex());
        this.out("Sending client: " + clientSocket.getRemoteSocketAddress() + " to server: " + serverToConnectTo);
        try {
            Socket socketToServer = new Socket(serverToConnectTo.getHost(), serverToConnectTo.getPort());
            Router.route(clientSocket, socketToServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // patch: there's no runnable to return :(. Fix will be needed in the future.
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

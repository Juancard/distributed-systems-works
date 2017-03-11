package tp1.ej1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class MyServer {

    private int port;

    public static void main(String[] args) {
        int port = 5001;
        MyServer myServer = new MyServer(port);
    }

    public MyServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.printf("Listening on port: %d...\n", port);
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted client: " + clientSocket.toString());

                String received = this.readMessage(clientSocket);
                System.out.println("Client: " + received);

                String toSend = String.format("I am server with message: \"%s\"",  received);
                System.out.println("Me: " + toSend);

                this.sendMessage(clientSocket, toSend);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readMessage(Socket clientSocket) throws IOException {
        InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isr);

        return bufferedReader.readLine();
    }

    public void sendMessage(Socket clientSocket, String messageToSend) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);

        printWriter.println(messageToSend);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

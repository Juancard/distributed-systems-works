package Tp1.Ex01.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:45
 */
public class MyClient {

    private String host;
    private int port;
    private Socket clientSocket;
    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;

    public MyClient(String host, int port){
        this.host = host;
        this.port = port;
        try {
            System.out.printf("Connecting to server %s:%d\n", host, port);

            this.clientSocket = new Socket(host, port);
            this.inputStreamReader = new InputStreamReader(this.clientSocket.getInputStream());
            this.outputStreamWriter = new OutputStreamWriter(this.clientSocket.getOutputStream());

            System.out.println("Client succesfully connected to: " + this.clientSocket.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String messageToSend){
        PrintWriter printWriter = new PrintWriter(this.outputStreamWriter, true);

        printWriter.println(messageToSend);
    }

    public String readMessage() throws IOException{
        BufferedReader bufferedReader = new BufferedReader(this.inputStreamReader);
        String received = bufferedReader.readLine();

        return received;
    }

    public void close() {
        try {
            this.outputStreamWriter.close();
            this.inputStreamReader.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}

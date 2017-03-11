package tp1.ej1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class MyClient {

    private String host;
    private int port;
    private Socket clientSocket;
    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5001;
        MyClient myClient = new MyClient(host, port);

        Scanner sc = new Scanner(System.in);
        System.out.printf("\nSend a Message to the Server\n");
        System.out.print("Me: ");
        String toSend = sc.nextLine();
        try {
            myClient.sendMessage(toSend);
            String received = myClient.readMessage();
            System.out.println("Server: " + received);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    public void sendMessage(String messageToSend) throws IOException{
        PrintWriter printWriter = new PrintWriter(this.outputStreamWriter, true);

        printWriter.println(messageToSend);
    }

    public String readMessage() throws IOException{
        BufferedReader bufferedReader = new BufferedReader(this.inputStreamReader);
        String received = bufferedReader.readLine();

        return received;
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

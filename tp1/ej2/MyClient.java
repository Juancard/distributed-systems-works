package tp1.ej2;

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
        String toSend;
        String exitWith = "-1";
        System.out.printf("\nSend a Message to the Server (%s to exit): \n", exitWith);
        while (true){
            System.out.print("Me: ");
            toSend = sc.nextLine();
            if (toSend.equals(exitWith)) break;
            try {
                myClient.sendMessage(toSend);
                String received = myClient.readMessage();

                if (received == null) {
                    System.out.println("Server has shutted down");
                    break;
                }

                System.out.println("Server: " + received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myClient.close();

    }

    private void close() {
        try {
            this.clientSocket.close();
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
            this.outputStreamWriter = new OutputStreamWriter(this.clientSocket.getOutputStream());
            this.inputStreamReader = new InputStreamReader(this.clientSocket.getInputStream());

            System.out.println("Client succesfully connected to: " + this.clientSocket.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String messageToSend) throws IOException{
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
        printWriter.println(messageToSend);
    }

    public String readMessage() throws IOException{
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

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

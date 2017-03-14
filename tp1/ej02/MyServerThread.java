package tp1.ej02;

import java.io.*;
import java.net.Socket;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class MyServerThread implements Runnable{

    private long threadId;
    private Socket clientSocket;
    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;

    public MyServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.outputStreamWriter = new OutputStreamWriter(this.clientSocket.getOutputStream());
            this.inputStreamReader = new InputStreamReader(this.clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true){
                // It stays block until a new message arrive
                String received = readMessage();
                if (received == null) break;
                System.out.println("Client: " + received);

                String toSend = String.format("I am thread n° %d, with message: \"%s\"", this.threadId, received);
                System.out.println("Me: " + toSend);

                sendMessage(toSend);
            }
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(this.inputStreamReader);
        return bufferedReader.readLine();
    }

    public void sendMessage(String messageToSend) throws IOException {
        PrintWriter printWriter = new PrintWriter(this.outputStreamWriter, true);
        printWriter.println(messageToSend);
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

}

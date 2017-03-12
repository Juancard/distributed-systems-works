package tp1.ej2;

import java.io.*;
import java.net.Socket;

/**
 * User: juan
 * Date: 11/03/17
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class MyServerThread implements Runnable{

    private Socket clientSocket;
    private long threadId;

    public MyServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(this.clientSocket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isr);

        return bufferedReader.readLine();
    }

    public void sendMessage(String messageToSend) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.clientSocket.getOutputStream());
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);

        printWriter.println(messageToSend);
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

}

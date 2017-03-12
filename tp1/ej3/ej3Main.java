package tp1.ej3;

import java.io.IOException;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class ej3Main {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5003;

        String userName1 = "user_1";
        String userName2 = "user_2";

        MyClient client_1 = new MyClient(host, port);
        Message client_1Message = new Message(
                String.format("I am %s. I send my regards to you, %s", userName1, userName2),
                userName1,
                userName2
        );
        System.out.println("Client_1 sending message to server");
        client_1.sendMessage(client_1Message);

    }

}

package Tp2.Ex01.Client;

import Tp2.Ex01.Common.FileProtocol;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 */
public class FileClient extends SocketClient {

    public FileClient(String host, int port) {
        super(host, port);
    }

    public String post() {
        this.sendToSocket(FileProtocol.POST);
        String result = this.readFromSocket().toString();
        return result;
    }

    public String del() {
        this.sendToSocket(FileProtocol.DEL);
        String result = this.readFromSocket().toString();
        return result;
    }

    public String get() {
        this.sendToSocket(FileProtocol.GET);
        String result = this.readFromSocket().toString();
        return result;
    }

    public String[] dir() {
        this.sendToSocket(FileProtocol.DIR);
        String[] result = (String[]) this.readFromSocket();
        return result;
    }

}

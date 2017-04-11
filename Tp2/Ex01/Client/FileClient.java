package Tp2.Ex01.Client;

import Tp2.Ex01.Common.FileProtocol;
import Tp2.Ex01.Common.TextFile;

import java.io.File;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 */
public class FileClient extends SocketClient {

    public FileClient(String host, int port) {
        super(host, port);
    }

    public boolean post(String fileName, String fileContent) {
        this.sendToSocket(FileProtocol.POST);
        this.sendToSocket(new TextFile(fileName, fileContent));
        Boolean result = (Boolean) this.readFromSocket();
        return result;
    }

    public String del() {
        this.sendToSocket(FileProtocol.DEL);
        String result = this.readFromSocket().toString();
        return result;
    }

    public String get(String fileName) {
        this.sendToSocket(FileProtocol.GET);
        this.sendToSocket(fileName);
        TextFile textFile = (TextFile) this.readFromSocket();
        return textFile.getContent();
    }

    public String[] dir() {
        this.sendToSocket(FileProtocol.DIR);
        String[] result = (String[]) this.readFromSocket();
        return result;
    }

}

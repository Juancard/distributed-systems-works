package Tp2.Ex01.Common;

import Common.TextFile;
import Common.Socket.SocketConnection;

/**
 * User: juan
 * Date: 14/03/17
 * Time: 11:51
 */
public class FileClient extends SocketConnection {

    public FileClient(String host, int port) {
        super(host, port);
    }

    public boolean post(String fileName, String fileContent) {
        this.send(FileProtocol.POST);
        this.send(new TextFile(fileName, fileContent));
        return (Boolean) this.read();
    }

    public boolean del(String fileName) {
        this.send(FileProtocol.DEL);
        this.send(fileName);
        return (Boolean) this.read();
    }

    public String get(String fileName) {
        this.send(FileProtocol.GET);
        this.send(fileName);
        TextFile textFile = (TextFile) this.read();
        return textFile.getContent();
    }

    public String[] dir() {
        this.send(FileProtocol.DIR);
        return (String[]) this.read();
    }

    public Object read(){
        try {
            return super.read();
        } catch (Exception e) {
            e.printStackTrace();
            this.out("Error in reading object from socket. Closing socket: " + this.getIdentity());
            this.close();
            return null;
        }
    }

}

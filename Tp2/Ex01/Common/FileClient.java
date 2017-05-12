package Tp2.Ex01.Common;

import Common.FileException;
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

    public boolean post(String fileName, String fileContent) throws Exception{
        this.send(FileProtocol.POST);
        this.send(new TextFile(fileName, fileContent));

        Object in = this.read();
        if (in instanceof Exception)
            throw (Exception) in;

        return (Boolean) in;
    }

    public boolean del(String fileName) throws Exception {
        this.send(FileProtocol.DEL);
        this.send(fileName);

        Object in = this.read();
        if (in instanceof Exception)
            throw (Exception) in;

        return (Boolean) in;
    }

    public String get(String fileName) throws Exception {
        this.send(FileProtocol.GET);
        this.send(fileName);

        Object in = this.read();
        if (in instanceof Exception)
            throw (Exception) in;

        TextFile textFile = (TextFile) in;
        return textFile.getContent();
    }

    public String[] dir() throws Exception {
        this.send(FileProtocol.DIR);

        Object in = this.read();
        if (in instanceof FileException)
            throw (FileException) in;
        if (in instanceof Exception)
            throw (Exception) in;

        return (String[]) in;
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

package Tp2.Ex01.Common;

/**
 * User: juan
 * Date: 08/04/17
 * Time: 14:10
 */
public class FileProtocol {
    public static final int READY = 0;

    public static final String POST = "POST";
    public static final String DEL = "DEL";
    public static final String GET = "GET";
    public static final String DIR = "DIR";

    private int state = READY;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

package Tp2.Ex02.Common;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 15:09
 */
public class AccountProtocol {
    public static final int AUTHENTICATING = 0;
    public static final int READY = 1;

    public static final String DEPOSIT = "DEPOSIT";
    public static final String EXTRACT = "EXTRACT";

    public static final String AUTHENTICATION_OK = "AUTHENTICATION_OK";

    private int state = AUTHENTICATING;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

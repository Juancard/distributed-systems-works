package Tp1.Ex03.Common;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class MessageProtocol {
    public static final int AUTHENTICATING = 0;
    public static final int READY = 1;

    public static final String SEND_NEW_MESSAGE = "SEND_NEW_MESSAGE";
    public static final String READ_MESSAGES = "READ_MESSAGES";

    public static final String AUTHENTICATION_OK = "AUTHENTICATION_OK";
    public static final String MESSAGE_ADDED = "MESSAGE_ADDED";
    public static final String READ_MESSAGES_END = "READ_MESSAGES_END";
    public static final String READ_MESSAGES_ACK = "READ_MESSAGES_ACK";

    private int state = AUTHENTICATING;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

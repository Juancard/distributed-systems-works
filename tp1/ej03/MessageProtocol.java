package tp1.ej03;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class MessageProtocol {
    public static final int CONNECTION_SET = 1;
    public static final int AUTHENTICATING = 2;
    public static final int READY = 3;

    public static final String AUTHENTICATION_OK = "AUTHENTICATION_OK";
    public static final String MESSAGE_SENT_OK = "MESSAGE_SENT_OK";
    public static final String READ_MESSAGES = "READ_MESSAGES";
    public static final String READ_MESSAGES_ACK = "READ_MESSAGES_ACK";

    private int state = CONNECTION_SET;
    private String userAuthenticated;

    public Object processInput(Object givenInput) {
        Object output = null;

        if (state == CONNECTION_SET){
            if (!(givenInput instanceof String)){
                return "Error: Username datatype is not valid";
            }
            output = givenInput;
            state = AUTHENTICATING;
        } else if (state == AUTHENTICATING) {
            // handle by server via method: this.isValidAuthentication
        } else if (state == READY){
            output = givenInput;
        }

        return output;
    }

    public void isValidAuthentication(boolean isValidAuthentication){
        if (isValidAuthentication) {
            state = READY;
        } else {
            state = CONNECTION_SET;
        }
    }

    public int getState() {
        return state;
    }
}

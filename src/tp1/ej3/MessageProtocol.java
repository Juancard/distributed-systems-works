package tp1.ej3;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class MessageProtocol {
    public static final int WAITING = 0;
    public static final int CONNECTION_SET = 1;
    public static final int AUTHENTICATING = 2;
    public static final int READY = 3;

    public static final String SEND_MESSAGE = "SEND";
    public static final String READ_MESSAGES = "READ_ALL";

    private int state = WAITING;
    private String userAuthenticated;

    public Object processInput(Object givenInput) {
        Object output = null;

        if (state == WAITING){
            output = "Connection set. What's your username?";
            this.state = CONNECTION_SET;
        } else if (state == CONNECTION_SET){
            if (!(givenInput instanceof String)){
                return "Error: Username datatype is not valid";
            }
            output = givenInput;
            state = AUTHENTICATING;
        } else if (state == AUTHENTICATING) {
            // handle by server via method: this.isValidAuthentication
        } else if (state == READY){
            return "Not yet implemented";
            /*
            if (givenInput.equals(SEND_MESSAGE)){
                if (!(givenInput instanceof Message))
                    return "Error: Not a valid message";
                output = givenInput;

            } else {
                return "Not a valid action to perform";
            }
            */
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

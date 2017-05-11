package Tp2.Ex07.Common;

/**
 * User: juan
 * Date: 10/05/17
 * Time: 20:29
 */
public class LoginException  extends Exception {

    public LoginException() { super(); }
    public LoginException(String message) { super(message); }
    public LoginException(String message, Throwable cause) { super(message, cause); }
    public LoginException(Throwable cause) { super(cause); }

}
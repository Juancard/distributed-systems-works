package Tp1.Ex07.Common;

/**
 * User: juan
 * Date: 24/03/17
 * Time: 17:57
 */
public class NotValidUserException extends Exception {
    public NotValidUserException() { super(); }
    public NotValidUserException(String message) { super(message); }
    public NotValidUserException(String message, Throwable cause) { super(message, cause); }
    public NotValidUserException(Throwable cause) { super(cause); }
}

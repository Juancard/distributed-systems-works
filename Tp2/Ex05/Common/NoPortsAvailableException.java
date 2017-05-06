package Tp2.Ex05.Common;

/**
 * User: juan
 * Date: 06/05/17
 * Time: 11:56
 */
public class NoPortsAvailableException extends Exception {

    public NoPortsAvailableException() { super(); }
    public NoPortsAvailableException(String message) { super(message); }
    public NoPortsAvailableException(String message, Throwable cause) { super(message, cause); }
    public NoPortsAvailableException(Throwable cause) { super(cause); }

}

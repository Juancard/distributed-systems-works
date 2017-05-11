package Tp2.Ex07.Common;

/**
 * User: juan
 * Date: 11/05/17
 * Time: 17:16
 */
public class PermissionException extends Exception{
    public PermissionException() { super(); }
    public PermissionException(String message) { super(message); }
    public PermissionException(String message, Throwable cause) { super(message, cause); }
    public PermissionException(Throwable cause) { super(cause); }
}

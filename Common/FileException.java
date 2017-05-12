package Common;

/**
 * User: juan
 * Date: 11/05/17
 * Time: 18:48
 */
public class FileException extends Exception {

    public FileException() { super(); }
    public FileException(String message) { super(message); }
    public FileException(String message, Throwable cause) { super(message, cause); }
    public FileException(Throwable cause) { super(cause); }

}

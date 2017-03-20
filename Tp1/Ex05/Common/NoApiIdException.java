package Tp1.Ex05.Common;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 19:53
 */
public class NoApiIdException extends Exception {

    public static final String DEFAULT_MESSAGE =
            "Error: No Weather API id specified.\n " +
            "Make Sure you've generated one and saved it in a java properties file.\n";

    public NoApiIdException() { super(); }
    public NoApiIdException(String message) { super(message); }
    public NoApiIdException(String message, Throwable cause) { super(message, cause); }
    public NoApiIdException(Throwable cause) { super(cause); }
}

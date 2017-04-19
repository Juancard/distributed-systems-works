package Tp2.Ex02.Common;

/**
 * User: juan
 * Date: 19/04/17
 * Time: 13:24
 */
public class BankException extends Exception {

        public BankException() { super(); }
        public BankException(String message) { super(message); }
        public BankException(String message, Throwable cause) { super(message, cause); }
        public BankException(Throwable cause) { super(cause); }

}

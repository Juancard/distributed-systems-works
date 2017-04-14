package Tp2.Ex01.Server;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: juan
 * Date: 13/04/17
 * Time: 16:36
 */
public class LogManager {
    private PrintStream toWriteIn;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public LogManager(PrintStream toWriteIn) {
        this.toWriteIn = toWriteIn;
        this.log("LogManager instantiated");
    }

    public void log(String message) {
        System.out.println(this.now() + message);
        toWriteIn.println(this.now() + ": " + message);
    }


    private Date now() {
        return new Timestamp(new Date().getTime());
    }
}

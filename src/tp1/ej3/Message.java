package tp1.ej3;

import java.io.Serializable;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Serializable{
    private String source;
    private String destination;
    private String value;

    public Message(String value, String source, String destination) {
        this.value = value;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Message{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

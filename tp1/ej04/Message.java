package tp1.ej04;

import java.io.Serializable;

/**
 * User: juan
 * Date: 12/03/17
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Serializable{
    private String from;
    private String to;
    private String body;

    public Message(String body, String from, String to) {
        this.body = body;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!body.equals(message.body)) return false;
        if (!from.equals(message.from)) return false;
        if (!to.equals(message.to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

package tp0.part_b;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class ListItem {
    private Object value;
    private ListItem next;

    public ListItem(Object value){
        this.value = value;
    }

    public boolean hasNext(){
        return next != null;
    }

    public void append(ListItem li){
        if (next != null)
            next.append(li);
        else
            next = li;
    }

    // GETTERS AND SETTERS
    public ListItem getNext() {
        return next;
    }

    public void setNext(ListItem next) {
        this.next = next;
    }
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

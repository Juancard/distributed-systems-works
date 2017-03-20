package Tp0.B;

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

    public void remove(){
        ListItem newNext = next.getNext();
        Object newValue = next.getValue();

        // copy reference
        next = this;

        next.setValue(newValue);
        next.setNext(newNext);
    }

    public void insertInThisPosition(ListItem li){
        Object auxValueThis = this.getValue();
        ListItem auxNextThis = this.getNext();
        Object auxValueLi = li.getValue();

        this.setValue(auxValueLi);
        this.setNext(li);
        li.setValue(auxValueThis);
        li.setNext(auxNextThis);
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

    @Override
    public String toString() {
        return "ListItem{" +
                "value=" + value +
                ", next=" + next +
                '}';
    }
}

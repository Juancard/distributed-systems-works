package tp0.B;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created with IntelliJ IDEA.
 * User: juan
 * Date: 06/03/17
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class LinkedListIterator implements Iterator<Object> {

    private LinkedList list;
    private ListItem currentItem;
    private boolean hasRemoved;
    private boolean hasCalledNext;

    public LinkedListIterator(LinkedList list){
        this.list = list;
        this.hasCalledNext = false;
        this.hasRemoved = false;
    }

    @Override
    public boolean hasNext() {
        if (currentItem == null)
            return !list.isEmpty();
        return currentItem.hasNext();
    }

    @Override
    public Object next() throws NoSuchElementException{
        if (list.isEmpty()) throw  new NoSuchElementException("List is empty");
        if (currentItem == null) {
            this.currentItem = list.getFirstElement();
        } else {
            this.currentItem = currentItem.getNext();
        }

        this.hasCalledNext = true;
        this.hasRemoved = false;

        return currentItem.getValue();
    }

}

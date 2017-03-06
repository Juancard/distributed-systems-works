package tp0.part_b;

import java.util.Iterator;
/**
 * User: juan
 * Date: 06/03/17
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public class LinkedList implements Iterable{
    private ListItem firstElement;

    public LinkedList(){}
    public LinkedList(Object value){
        this.firstElement = new ListItem(value);
    }

    public void add(Object value){
        ListItem newListItem = new ListItem(value);

        if (this.firstElement == null)    {
            this.firstElement = newListItem;
        } else {
            firstElement.append(newListItem);
        }

    }

    public boolean isEmpty(){
        return this.firstElement == null;
    }

    @Override
    public Iterator iterator() {
        return new LinkedListIterator(this);
    }


    public ListItem getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(ListItem firstElement) {
        this.firstElement = firstElement;
    }
}

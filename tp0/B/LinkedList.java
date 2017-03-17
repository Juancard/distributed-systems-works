package tp0.B;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
/**
 * Date: 06/03/17
 * Time: 13:31
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

    public void sort(Comparator comparator){
        Iterator iterator = this.iterator();
        ArrayList<Object> listItemsToOrder = new ArrayList<Object>();
        while (iterator.hasNext()){
            listItemsToOrder.add(iterator.next());
        }
        Collections.sort(listItemsToOrder, comparator);
        iterator = listItemsToOrder.iterator();
        if (iterator.hasNext()) {
            ListItem previousListItem = new ListItem(iterator.next());
            this.firstElement = previousListItem;
            while (iterator.hasNext()){
                ListItem actualListItem = new ListItem(iterator.next());
                previousListItem.setNext(actualListItem);
                previousListItem = actualListItem;
            }
        }
    }


    public ListItem getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(ListItem firstElement) {
        this.firstElement = firstElement;
    }
}

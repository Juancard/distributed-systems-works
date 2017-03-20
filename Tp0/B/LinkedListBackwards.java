package Tp0.B;

/**
 * Created with IntelliJ IDEA.
 * User: juan
 * Date: 06/03/17
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class LinkedListBackwards extends LinkedList {

    @Override
    public void add(Object value){
        ListItem newListItem = new ListItem(value);
        newListItem.setNext(this.getFirstElement());
        this.setFirstElement(newListItem);
    }

}

package Tp0.B;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class LinkedListTest {
    @Test
    public void constructs(){
        LinkedList ll = new LinkedList();
        Assert.assertNotNull(ll);
    }
    @Test
    public void adds(){
        LinkedList ll = new LinkedList();

        String toAdd = "45";
        ll.add(toAdd);

        Assert.assertEquals(ll.getFirstElement().getValue(), toAdd);
    }
    @Test
    public void iterates(){
        LinkedList ll = new LinkedList();

        Object[] toAdd = {1, "hola", 3};

        for (int i= 0; i<toAdd.length; i++){
            ll.add(toAdd[i]);
        }

        Iterator iterator = ll.iterator();
        int mockedIndex = 0;
        while (iterator.hasNext()){
            Object theNext = iterator.next();
            Assert.assertEquals(theNext, toAdd[mockedIndex]);
            mockedIndex++;
        }
    }
    @Test
    public void iteratesBackwards(){
        LinkedList linkedListBackwards = new LinkedListBackwards();

        Object[] toAdd = {1, "hola", 3};

        for (int i= 0; i<toAdd.length; i++){
            linkedListBackwards.add(toAdd[i]);
        }

        Iterator iterator = linkedListBackwards.iterator();
        int mockedIndex = toAdd.length - 1;
        while (iterator.hasNext()){
            Object theNext = iterator.next();
            Assert.assertEquals(theNext, toAdd[mockedIndex]);
            mockedIndex--;
        }
    }

    @Test
    public void removes(){
        LinkedList linkedList = new LinkedList();

        Object[] toAdd = {1, "hola", 3};
        Object[] afterRemoved = {1, 3};

        // Adds all elements
        for (int i= 0; i<toAdd.length; i++){
            linkedList.add(toAdd[i]);
        }

        // Removes elements
        linkedList.getFirstElement().getNext().remove();

        Iterator iterator = linkedList.iterator();
        int mockedIndex = 0;
        while (iterator.hasNext()){
            Object theNext = iterator.next();
            Assert.assertEquals(theNext, afterRemoved[mockedIndex]);
            mockedIndex++;
        }
    }

    @Test
    public void insertsInPosition(){
        LinkedList linkedList = new LinkedList();

        Object[] toAdd = {1, "hola", 3};
        ListItem toInsert = new ListItem(2);
        Object[] afterInsertion = {1, 2, "hola", 3};

        // Adds all elements
        for (int i= 0; i<toAdd.length; i++){
            linkedList.add(toAdd[i]);
        }

        // insert elements
        linkedList.getFirstElement().getNext().insertInThisPosition(toInsert);

        Iterator iterator = linkedList.iterator();
        int mockedIndex = 0;
        while (iterator.hasNext()){
            Object theNext = iterator.next();
            Assert.assertEquals(theNext, afterInsertion[mockedIndex]);
            mockedIndex++;
        }
    }
}

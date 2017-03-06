package tp0.part_b;

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

        Assert.assertEquals(ll.firstElement.getValue(), toAdd);
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
}

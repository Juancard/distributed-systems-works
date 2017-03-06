package tp0.part_b;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class ListItemTest {
    @Test
    public void constructor() {
        Object o = "Hello world";
        ListItem li = new ListItem(o);
        Assert.assertNotNull(li);
    }
    @Test
    public void setNext() {
        Object o = "Hello world";
        ListItem li = new ListItem(o);

        Object o2 = 2;
        ListItem liNext = new ListItem(o2);

        li.setNext(liNext);

        Assert.assertEquals(li.getNext(), liNext);
    }

    @Test
    public void hasNext() {
        Object o = "Hello world";
        ListItem li = new ListItem(o);

        Object o2 = 2;
        ListItem liNext = new ListItem(o2);

        li.setNext(liNext);

        Assert.assertTrue(li.hasNext());
        Assert.assertFalse(liNext.hasNext());
    }

    @Test
    public void append() {
        Object o = "Hello world";
        ListItem li = new ListItem(o);

        Object o2 = 2;
        ListItem li2 = new ListItem(o2);

        Object o3 = "";
        ListItem li3 = new ListItem(o3);

        li.append(li2);
        li.append(li3);

        Assert.assertEquals(li.getNext(), li2);
        Assert.assertEquals(li2.getNext(), li3);
    }
}

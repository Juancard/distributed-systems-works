package tp0.part_b;

import junit.framework.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class AgendaTest {
    @Test
    public void constructs(){
        String nombre = "Juan";
        String telefono = "422232";
        Agenda agenda = new Agenda(nombre, telefono);
        Assert.assertNotNull(agenda);
    }
    @Test
    public void setsAndGets(){
        String nombre = "Juan";
        String telefono = "422232";
        Agenda agenda = new Agenda(nombre, telefono);
        Assert.assertEquals(agenda.getNombre(), nombre);
        Assert.assertEquals(agenda.getTelefono(), telefono);
    }
    @Test
    public void compares(){
        String nombre1 = "Juan";
        String telefono1 = "422232";
        Agenda agenda1 = new Agenda(nombre1, telefono1);

        String nombre2 = "Pedro";
        String telefono2 = "432939";
        Agenda agenda2 = new Agenda(nombre2, telefono2);

        Assert.assertTrue(agenda1.compareTo(agenda2) < 0);
    }
    @Test
    public void sortsArray(){
        String nombre1 = "Juan";
        String telefono1 = "422232";
        Agenda agenda1 = new Agenda(nombre1, telefono1);

        String nombre2 = "Pedro";
        String telefono2 = "432939";
        Agenda agenda2 = new Agenda(nombre2, telefono2);

        String nombre3 = "Adolfo";
        String telefono3 = "495431";
        Agenda agenda3 = new Agenda(nombre3, telefono3);

        Agenda[] a = {agenda1, agenda2, agenda3};
        Arrays.sort(a);

        Assert.assertNotSame(a[0].getNombre(), agenda1.getNombre());
        Assert.assertSame(a[0].getNombre(), agenda3.getNombre());
    }
}

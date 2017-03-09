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
        Contacto contacto = new Contacto(nombre, telefono);
        Assert.assertNotNull(contacto);
    }
    @Test
    public void setsAndGets(){
        String nombre = "Juan";
        String telefono = "422232";
        Contacto contacto = new Contacto(nombre, telefono);
        Assert.assertEquals(contacto.getNombre(), nombre);
        Assert.assertEquals(contacto.getTelefono(), telefono);
    }
    @Test
    public void compares(){
        String nombre1 = "Juan";
        String telefono1 = "422232";
        Contacto contacto1 = new Contacto(nombre1, telefono1);

        String nombre2 = "Pedro";
        String telefono2 = "432939";
        Contacto contacto2 = new Contacto(nombre2, telefono2);

        Assert.assertTrue(contacto1.compareTo(contacto2) < 0);
    }
    @Test
    public void sortsArray(){
        String nombre1 = "Juan";
        String telefono1 = "422232";
        Contacto contacto1 = new Contacto(nombre1, telefono1);

        String nombre2 = "Pedro";
        String telefono2 = "432939";
        Contacto contacto2 = new Contacto(nombre2, telefono2);

        String nombre3 = "Adolfo";
        String telefono3 = "495431";
        Contacto contacto3 = new Contacto(nombre3, telefono3);

        Contacto[] a = {contacto1, contacto2, contacto3};
        Arrays.sort(a);

        Assert.assertNotSame(a[0].getNombre(), contacto1.getNombre());
        Assert.assertSame(a[0].getNombre(), contacto3.getNombre());
    }
}

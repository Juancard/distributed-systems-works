package tp0.part_b;

import java.util.Comparator;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class Contacto implements Comparable<Contacto>{
    private String nombre;
    private String telefono;

    public Contacto(String nombre, String telefono){
        this.nombre = nombre;
        this.telefono = telefono;
    }

    @Override
    public int compareTo(Contacto givenContacto){
        String nombre1 = givenContacto.getNombre().toLowerCase();
        String nombre2 = this.getNombre().toLowerCase();
        return nombre2.compareTo(nombre1);
    }

    public static Comparator<Contacto> nombreComparator = new Comparator<Contacto>(){
        public int compare(Contacto c1, Contacto c2) {
            return c1.compareTo(c2);
        }
    };

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre + ": " + this.telefono;
    }
}

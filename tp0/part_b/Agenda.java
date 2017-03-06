package tp0.part_b;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class Agenda implements Comparable<Agenda>{
    private String nombre;
    private String telefono;

    public Agenda(String nombre, String telefono){
        this.nombre = nombre;
        this.telefono = telefono;
    }

    @Override
    public int compareTo(Agenda givenAgenda){
        String nombre1 = givenAgenda.getNombre().toUpperCase();
        String nombre2 = this.getNombre().toUpperCase();
        return nombre2.compareTo(nombre1);
    }

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

}

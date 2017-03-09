package tp0.part_b;

import java.util.*;

/**
 * User: juan
 * Date: 06/03/17
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 */
public class AgendaMain {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        LinkedList agenda = new LinkedList();
        agenda = loadContacts(agenda);
        agenda = sortContacts(agenda);

        String opcion;
        boolean salir = false;
        showMenuHeader();

        while (!salir){
            showMenu();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
                System.out.println("\n------ Nuevo Contacto -----\n");
                Contacto contacto = ingresarContacto();
                agenda.add(contacto);
                pause();
            } else if (opcion.equals("2")){
                System.out.println("\n---- Lista de Contactos ---\n");
                Iterator iterator = agenda.iterator();
                while (iterator.hasNext()){
                    System.out.println(iterator.next());
                }
                pause();
            } else if (opcion.equals("3")){
                System.out.println("\n--- Buscar por nombre ---\n");
                System.out.print("Nombre a buscar: ");
                String nombreToSearch = sc.nextLine().toLowerCase();
                Iterator iterator = agenda.iterator();
                ArrayList<Contacto> found = new ArrayList<Contacto>();
                while (iterator.hasNext()){
                    Contacto contactoBeingSearch = (Contacto) iterator.next();
                    String nombreBeingSearch = contactoBeingSearch.getNombre().toLowerCase();
                    if (nombreBeingSearch.contains(nombreToSearch))
                        found.add(contactoBeingSearch);
                }
                showSearchResults(found);

                pause();
            } else if (opcion.equals("4")){
                System.out.println("\n--- Buscar por telefono ---\n");
                System.out.print("Telefono a buscar: ");
                String telefonoToSearch = sc.nextLine().toLowerCase();
                Iterator iterator = agenda.iterator();
                ArrayList<Contacto> found = new ArrayList<Contacto>();
                while (iterator.hasNext()){
                    Contacto contactoBeingSearch = (Contacto) iterator.next();
                    String telefonoBeingSearch = contactoBeingSearch.getTelefono().toLowerCase();
                    if (telefonoBeingSearch.contains(telefonoToSearch))
                        found.add(contactoBeingSearch);
                }
                showSearchResults(found);
                pause();
            }
        }
    }

    private static void showSearchResults(ArrayList<Contacto> results) {
        if (results.isEmpty()) {
            System.out.println("No hay resultados para la búsqueda");
        } else {
            Iterator iterator = results.iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next());
            }
        }
    }

    private static LinkedList sortContacts(LinkedList agenda) {
        agenda.sort(Contacto.nombreComparator);
        return agenda;
    }

    private static LinkedList loadContacts(LinkedList agenda) {
        agenda.add(new Contacto("Pedro Alvarez", "420022"));
        agenda.add(new Contacto("Alberto Zaragoza", "440247"));
        agenda.add(new Contacto("Ricardo Marz", "475052"));
        return agenda;
    }

    private static Contacto ingresarContacto() {

        System.out.print("Nombre de contacto: ");
        String nombre = sc.nextLine();

        System.out.print("Telefono de contacto: ");
        String tel = sc.nextLine();

        return new Contacto(nombre, tel);
    }

    private static void showMenuHeader() {
        System.out.println("*******************************************************");
        System.out.println("Sistemas Distribuidos y Programación Paralela");
        System.out.println("TP N°0 - Parte B");
        System.out.println("*******************************************************\n");
    }

    private static void showMenu(){
        System.out.println("Agenda - Menú");
        System.out.println("1 - Nuevo contacto");
        System.out.println("2 - Ver contactos");
        System.out.println("3 - Buscar por nombre");
        System.out.println("4 - Buscar por telefono");
        System.out.println("0 - Salir");
        System.out.print("Ingrese opción: ");
    }

    private static void pause() {
        System.out.print("\nPresione cualquier tecla para continuar ");
        sc.nextLine();
        System.out.println();
    }
}

package tp0.part_b;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        LinkedList agenda = readFromFile();
        //agenda = loadContacts(agenda);

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
                agenda = sortContacts(agenda);
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
        writeToFile(agenda);
    }

    private static LinkedList readFromFile() {
        // The name of the file to open.
        String fileName = "src/tp0/part_b/agenda.txt";
        LinkedList agenda = new LinkedList();

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] lineSplitted = line.split(";");
                String nombre = lineSplitted[0];
                String tel =  lineSplitted[1];
                Contacto c = new Contacto(nombre, tel);
                agenda.add(c);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            // If file does not exists, just return empty agenda
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        } finally {
            return agenda;
        }
    }

    private static void writeToFile(LinkedList agenda) {


        // The name of the file to open.
        String fileName = "src/tp0/part_b/agenda.txt";

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            Iterator i = agenda.iterator();
            while (i.hasNext()){
                Contacto c = (Contacto) i.next();
                bufferedWriter.write(c.getNombre() + ";" + c.getTelefono());
                bufferedWriter.newLine();
            }

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
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

package tp0.C;

import java.util.Scanner;

/**
 * User: juan
 * Date: 10/03/17
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class MyTv {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Tv myTv = new Tv();

        String opcion;
        boolean salir = false;
        showMenuHeader();

        while (!salir){

            myTv.powerOnOff();

            showMenu();
            opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
                myTv.powerOnOff();
            } else if (opcion.equals("1")){
                System.out.println("\nIngresar Canal\n");
                pause();
            }
        }
    }

    private static void showMenuHeader() {
        System.out.println("*******************************************************");
        System.out.println("Sistemas Distribuidos y Programación Paralela");
        System.out.println("TP N°0 - Parte C");
        System.out.println("*******************************************************\n");
    }

    private static void showMenu(){
        System.out.println("MyTV - Menú");
        System.out.println("1 - Ingresar Canal");
        System.out.println("2 - Subir Volumen");
        System.out.println("3 - Bajar Volumen");
        System.out.println("4 - Subir Canal");
        System.out.println("5 - Bajar Canal");
        System.out.println("6 - Silenciar Canal");
        System.out.println("0 - Apagar TV");
        System.out.print("Ingrese opción: ");
    }

    private static void pause() {
        System.out.print("\nPresione cualquier tecla para continuar ");
        sc.nextLine();
        System.out.println();
    }
}

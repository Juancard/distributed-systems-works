package tp0.part_a;

import java.util.Scanner;

public class Main {

	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws DenominadorCero {
		Fraccion f1 = new Fraccion();
		Fraccion f2 = new Fraccion();

		String opcion;
		boolean salir = false;
		showMenuHeader();

		while (!salir){
			showMenu();
			opcion = sc.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
				f1 = ingresarFraccion();
				pause();
            } else if (opcion.equals("2")){
                f2 = ingresarFraccion();
                pause();
            } else if (opcion.equals("3")){
                System.out.println(f1.toString() + " + " + f2.toString() + " = " + f1.sumar(f2));
                pause();
            } else if (opcion.equals("4")) {
                System.out.println(f1.toString() + " - " + f2.toString() + " = " + f1.restar(f2));
                pause();
            } else if (opcion.equals("5")){
                int comparison = f1.compareTo(f2);
                String comparisonString;

                if (comparison > 0){
                    comparisonString = " es mayor que ";
                } else if (comparison < 0){
                    comparisonString = " es menor que ";
                } else {
                    comparisonString = " es igual a ";
                }

                System.out.println(f1.toString() + comparisonString + f2.toString());
                pause();
            } else {
                System.out.println("Opción no válida");
            }
		}
		System.out.println("Adios!");
	}

	private static Fraccion ingresarFraccion() {
		Fraccion f = new Fraccion();
		
		try{
			
			System.out.println("Numerador: ");
			f.setNumerador(Integer.parseInt(sc.nextLine()));
			
			System.out.println("Denominador: ");
			f.setDenominador(Integer.parseInt(sc.nextLine()));
			
		} catch (NumberFormatException e){
			System.out.println("Valor ingresado no es un entero válido");
			
		} catch (DenominadorCero e){
			System.out.println("Denominador no puede ser cero");
		} 
				
		return f;
	}

	private static void pause() {
		System.out.println("\nPresione cualquier tecla para continuar");
		sc.nextLine();		
	}

	private static void showMenuHeader() {
		System.out.println("*******************************************************");
		System.out.println("Sistemas Distribuidos y Programación Paralela");
		System.out.println("TP N°0 - Parte A");
		System.out.println("*******************************************************\n");		
	}

	private static void showMenu(){
		System.out.println("Fracción - Menú");
		System.out.println("1 - Ingresar fracción I");
		System.out.println("2 - Ingresar fracción II");
		System.out.println("3 - Sumar");
		System.out.println("4 - Restar");
		System.out.println("5 - Comparar");
		System.out.println("0 - Salir");
		System.out.println("Ingrese opción: ...");
	}
	
}

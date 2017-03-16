package tp0.C;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * User: juan
 * Date: 10/03/17
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class MyTv {
    private static Scanner sc = new Scanner(System.in);
    private static Tv myTv;

    public static void main(String[] args) {
        myTv = new Tv();
        showMenuHeader();
        while (myTv.isPowerOn()){
            showMenu();
            
            handleMainOption(sc.nextLine());
            System.out.println("");
            System.out.println(myTv.toString());
            
            if (myTv.isPowerOn()) pause();
        }
    }

    private static void handleMainOption(String opcion) {
        if (opcion.equals("0")) {
            myTv.powerOnOff();
        } else if (opcion.equals("1")){
            System.out.println("\nIngresar Canal\n");
            enterChannel();
        } else if (opcion.equals("2")){
            System.out.println("\nSubir de Canal\n");
            myTv.channelUp();
        } else if (opcion.equals("3")){
            System.out.println("\nBajar de Canal\n");
            myTv.channelDown();
        } else if (opcion.equals("4")){
            System.out.println("\nSubir volumen\n");
            myTv.volumeUp();
        } else if (opcion.equals("5")){
            System.out.println("\nBajar volumen\n");
            myTv.volumeDown();
        } else if (opcion.equals("6")){
            System.out.println("\nSilenciar\n");
            myTv.muteOnOff();
        }		
	}

	private static void enterChannel() {
    	int currentChannel = myTv.getChannel();
    	boolean salir = false;
    	
    	while (currentChannel == myTv.getChannel() && !salir) {
    		String givenDigit = askChannelDigit();
        	if (!Arrays.asList(myTv.validDigits).contains(givenDigit)) {
        		salir = true;
        	} else {
        		callTvDigit(givenDigit);            		
    			showDigitsBeingSet();
        	}
    	}

	}
    
    private static void callTvDigit(String givenDigit) {
    	String method = "number" + givenDigit;
		try {
			myTv.getClass().getMethod(method).invoke(myTv);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			System.out.println("Error seteando digito de canal en su Tv. Vuelva a intentarlo más tarde.");
		}		
	}

	private static void showDigitsBeingSet() {
    	String ingresado = myTv.getChannelBeingSet();
		if (ingresado.length() > 0){
			System.out.println("\nIngresado: " + myTv.getChannelBeingSet());
			pause();
		}		
	}

	private static String askChannelDigit(){
    	System.out.println("Digitos: ");
    	for (String number : myTv.validDigits) {
    		System.out.println(number + ": Number " + number);
    	}
    	System.out.println("Otro Valor: Salir");
    	System.out.print("Enter digit: ");        	
    	return sc.nextLine();
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
        System.out.println("2 - Subir Canal");
        System.out.println("3 - Bajar Canal");
        System.out.println("4 - Subir Volumen");
        System.out.println("5 - Bajar Volumen");
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

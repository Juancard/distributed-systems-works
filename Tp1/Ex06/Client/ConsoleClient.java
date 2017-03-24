package Tp1.Ex06.Client;

import Common.CommonMain;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:17
 */
public class ConsoleClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5006;
    private static final int EXERCISE_NUMBER = 6;
    private static final int TP_NUMBER = 1;

    private static final int DEFAULT_VECTOR_SIZE = 3;

    private static Scanner scanner = new Scanner(System.in);
    private static VectorClient vectorClient;

    public static void main(String[] args) {
        CommonMain.showWelcomeMessage(TP_NUMBER, EXERCISE_NUMBER, "RMI Vector Service");
        try {
            vectorClient = newClient();
        } catch (Exception e) {
            CommonMain.display("Error starting RMI client: " + e.toString());
            System.exit(1);
        }
        handleMainOptions();
    }

    private static VectorClient newClient() throws RemoteException, NotBoundException {
        String host = CommonMain.askForHost(DEFAULT_HOST);
        int port = CommonMain.askForPort(DEFAULT_PORT);
        return new VectorClient(host, port);
    }

    private static void handleMainOptions() {
        String opcion;
        boolean salir = false;

        while (!salir) {
            showMain();
            opcion = scanner.nextLine();
            if (opcion.equals("0")) {
                salir = true;
            } else if (opcion.equals("1")){
                CommonMain.createSection("Vector Addition");
                try {
                    handleVectorAdd();
                } catch (RemoteException e) {
                    CommonMain.display("Error in adding vectors: " + e.toString());
                }
                CommonMain.pause();
            } else if (opcion.equals("2")){
                CommonMain.createSection("Vector Substraction");
                handleVectorSubstract();
                CommonMain.pause();
            }
        }
    }

    private static void showMain(){
        CommonMain.createSection("Vector Service - Main");
        CommonMain.display("1 - Addition");
        CommonMain.display("2 - Substraction");
        CommonMain.display("0 - Exit");
        CommonMain.display("");
        System.out.print("Select option: ");
    }

    private static void handleVectorAdd() throws RemoteException {
        int vectorSize = askVectorSize();

        CommonMain.display("Enter vector 'A': ");
        int[] vectorA = askForVector(vectorSize);

        CommonMain.display("Enter vector 'B': ");
        int[] vectorB = askForVector(vectorSize);

        try {
            int[] additionResult = vectorClient.add(vectorA, vectorB);
            showVector(additionResult);
        } catch (RemoteException e) {
            throw new RemoteException("Could not add vectors: " + e.toString());
        }
    }

    private static int askVectorSize() {
        CommonMain.display("Enter vector size [3]: ");

        int size;
        String givenSize = scanner.nextLine();

        if (givenSize.length() == 0)
            return DEFAULT_VECTOR_SIZE;

        try {
            return Integer.parseInt(givenSize);
        } catch(NumberFormatException e){
            System.out.println("Not a valid vector size. Default size has been set.");
            return DEFAULT_VECTOR_SIZE;
        }
    }

    private static int[] askForVector(int vectorSize) {
        int[] vector = new int[vectorSize];

        for (int i=0; i < vectorSize; i++){
            System.out.print("Value " + (i+1) + ": ");
            vector[i] = scanner.nextInt();
        }

        return vector;
    }

    private static void showVector(int[] vector) {
        CommonMain.display("Result is: ");
        String out = "";
        for (int i=0; i<vector.length; i++) {
            out += vector[i];
            if (i != vector.length - 1)
                out += " - ";
        }
        CommonMain.display(out);
    }

    private static void handleVectorSubstract() {

    }

}

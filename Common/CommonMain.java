package Common;

import java.util.Scanner;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 16:03
 */
public class CommonMain {
    private static Scanner scanner = new Scanner(System.in);

    public static String askForHost(String defaultIp){
        System.out.printf("Enter server Ip or host [%s]: ", defaultIp);
        String ip = scanner.nextLine();
        if (ip.length() == 0)
            ip = defaultIp;
        return ip;
    }

    public static int askForPort(int defaultPort){
        System.out.printf("Enter server Port [%s]: ", defaultPort);
        String givenPort = scanner.nextLine();
        return processGivenPort(givenPort, defaultPort);
    }

    public static int askForPort(String toDisplay, int defaultPort){
        System.out.printf("%s [%s]: ", toDisplay, defaultPort);
        String givenPort = scanner.nextLine();
        return processGivenPort(givenPort, defaultPort);
    }

    public static int processGivenPort(String givenPort, int defaultPort){
        if (givenPort.length() == 0) return defaultPort;
        else
            try {
               return Integer.parseInt(givenPort);
            } catch(NumberFormatException e){
                display("Not a valid port number. Default port has been set.");
                return defaultPort;
            }
    }

    public static void showWelcomeMessage(int tpNumber, int exerciseNumber, String title) {
        String header = "Sistemas Distribuidos y Programación Paralela";
        String subtitle = String.format("TP N° %d - Ex. N° %d", tpNumber, exerciseNumber);
        createSection(header + "\n" + subtitle + "\n" + title);
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static void createSection(String section){
        String separatorChar = "*";
        int separatorMinLength = 6;
        int longestString = -1;

        for (String s : section.split("\n"))
            if (s.length() > longestString) longestString = s.length();

        String separator = repeat(separatorMinLength + longestString, separatorChar);
        showSection(separator, section);
    }

    public static void showSection(String separator, String section) {
        display("");
        display(separator);
        display(section);
        display(separator);
        display("");
    }

    public static void pause() {
        System.out.println("\nPresione ENTER para continuar");
        scanner.nextLine();
    }

    public static void display(String toDisplay){
        System.out.println(toDisplay);
    }
}

/*
    StartupPrompt.java

    Client that sends messages to the ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public abstract class StartupPrompt {

    public static InetAddress clientAddressPrompt() throws UnknownHostException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String addressString = scan.next();

        byte[] address = stringToAddress(addressString);
        InetAddress serverAddress = null;
        serverAddress = InetAddress.getByAddress(address);
        return serverAddress;
    }

    public static int clientPortNumberPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server Port: ");
        int serverPortNumber = scan.nextInt();
        return serverPortNumber;
    }

    private static byte[] stringToAddress(String string) {
        String[] digits = string.split("\\.");
        byte[] address = new byte[digits.length];
        for (int i = 0; i < digits.length; i++) {
            Integer current = new Integer(digits[i]);
            address[i] = current.byteValue();
        }
        return address;
    }

    public static int serverPortNumberPrompt() {
        InetAddress address = InetAddress.getLoopbackAddress();

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Local IP Address: " + address);
            System.out.println("External IP Address: ###.###.###.###");
            System.out.print("Port Number: ");
            try {
                int portNumber = scan.nextInt();
                System.out.println();
                return portNumber;
            } catch (InputMismatchException e) {
                scan.nextLine();
            }
        }
    }
}
/*
    ChatServerConfiguration.java

    Collect needed configuration information for ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerConfiguration {

    private static final int DEFAULT_PORT = 9000;

    private final InetAddress address = InetAddress.getLoopbackAddress();

    private InetAddress externalAddress;
    private int portNumber;

    ChatServerConfiguration() {
        portNumber = portNumberPrompt();
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPortNumber() {
        return portNumber;
    }

    // private String initExternalAddress() {
    //     String externalInetAddress = ExternalAddressService.whatIsMyIp();
    //     if (externalInetAddress == null) {
    //         externalInetAddress = "";
    //     }

    //     return "###.###.###.###";
    // }

    private int portNumberPrompt() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Local IP Address: " + address);
            System.out.println("External IP Address: ###.###.###.###");
            System.out.print("Port Number: ");
            try {
                String portNumberString = scan.next();

                if (portNumberString.equals("_")) {
                    System.out.println();
                    return DEFAULT_PORT;
                }

                System.out.println();
                return Integer.parseInt(portNumberString);
            } catch (InputMismatchException e) {
                scan.nextLine();
            }
        }
    }
}
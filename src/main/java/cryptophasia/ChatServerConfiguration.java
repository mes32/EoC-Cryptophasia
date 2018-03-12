/*
    ChatServerConfiguration.java

    Collect needed configuration information for ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServerConfiguration {

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
                int portNumber = scan.nextInt();
                System.out.println();
                return portNumber;
            } catch (InputMismatchException e) {
                scan.nextLine();
            }
        }
    }
}
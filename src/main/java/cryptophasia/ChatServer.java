/*
    ChatServer.java

    Instant messaging server
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    public static final String SUBMITNAME = "SUBMITNAME";
    public static final String NAMEACCEPT = "NAMEACCEPT";
    public static final String SHUTDOWN = "SHUTDOWN";

    private int userCount = 0;
    private ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
    private int portNumber;
    private ServerSocket listener;

    ChatServer() {
        String internalAddress = getInternalAddress();
        String externalAddress = getExternalAddress();
        portNumber = portNumberPrompt(internalAddress, externalAddress);
        listener = openConnection(portNumber);
    }

    public void run() {
        try {
            while (true) {
                try {
                    Socket socket = listener.accept();
                    display(" + Socket connection accepted");
                    new ChatClientHandler(socket, this).start();
                } catch (IOException e) {
                    display(" + Socket connection refused");
                }
            }
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void addWriter(PrintWriter writer) {
        printWriters.add(writer);
    }

    public void removeWriter(PrintWriter writer) {
        printWriters.remove(writer);
    }

    private String getInternalAddress() {
        try {
            InetAddress ipLocalHost = InetAddress.getLocalHost();
            return ipLocalHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return "###.###.###.###";
    }

    private String getExternalAddress() {
        // String externalInetAddress = ExternalAddressService.whatIsMyIp();
        // if (externalInetAddress == null) {
        //     externalInetAddress = "";
        // }

        return "###.###.###.###";
    }

    private int portNumberPrompt(String internalAddress, String externalAddress) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Local IP Address: " + internalAddress);
            System.out.println("External IP Address: " + externalAddress);
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

    private ServerSocket openConnection(int portNumber) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatServer was unable to open connection.");
            System.exit(1);
        }
        display(" + Server now listening on port: " + portNumber);
        return listener;
    }

    public void display(String message) {
        System.out.println(message);
        for (PrintWriter writer : printWriters) {
            writer.println(message);
        }
    }

    public int getPortNumber() {
        return portNumber;
    }
}
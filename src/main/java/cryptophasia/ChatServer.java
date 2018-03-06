/*
    ChatServer.java

    Instant messaging server
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {
    public static final String SUBMITNAME = "SUBMITNAME";
    public static final String NAMEACCEPT = "NAMEACCEPT";
    public static final String SHUTDOWN = "SHUTDOWN";

    private int userCount = 0;
    private ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
    private InetAddress address;
    private int port;
    private ServerSocket listener;

    ChatServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        listener = openConnection(port);
    }

    public void run() {
        try {
            while (true) {
                try {
                    Socket socket = listener.accept();
                    ServerNotificationMessage connectionAcception = new ServerNotificationMessage("Socket connection accepted");
                    display(connectionAcception);
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

    public void display(String message) {
        System.out.println(message);
        for (PrintWriter writer : printWriters) {
            writer.println(message);
        }
    }

    public void display(AbstractMessage message) {
        System.out.println(message.toString());
        for (PrintWriter writer : printWriters) {
            writer.println(message.transmit());
        }
    }

    public int getPortNumber() {
        return port;
    }

    public void addWriter(PrintWriter writer) {
        printWriters.add(writer);
    }

    public void removeWriter(PrintWriter writer) {
        printWriters.remove(writer);
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
}
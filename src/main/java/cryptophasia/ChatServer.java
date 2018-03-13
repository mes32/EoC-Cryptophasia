/*
    ChatServer.java

    Instant messaging server
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {

    private ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
    private InetAddress address;
    private int port;
    private ServerSocket listener;

    ChatServer(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;
        listener = openConnection(port);
    }

    public void run() {
        try {
            while (true) {
                try {
                    Socket socket = listener.accept();
                    new ChatClientHandler(socket, this).start();
                    display(new ServerNotificationMessage("New client connected"));
                } catch (IOException e) {
                    display(new ServerNotificationMessage("New client refused"));
                }
            }
        } finally {
            try {
                listener.close();
            } catch (IOException ignore) {
                // Ignoring IOException
            }
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

    private ServerSocket openConnection(int portNumber) throws IOException {
        ServerSocket listener = new ServerSocket(portNumber);
        display(new ServerNotificationMessage("Server now listening on port: " + portNumber));
        return listener;
    }
}
/*
    DateServer.java

    Simple date server class
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class DateServer {

    private static final int PORT_NUMBER = 9090;

    DateServer() {

        try {
            InetAddress ipLocalHost = InetAddress.getLocalHost();

            System.out.println();
            System.out.println("IP Address: " + ipLocalHost.getHostAddress());
            System.out.println("Port Number: " + PORT_NUMBER);
            System.out.println();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ServerSocket listener = new ServerSocket(PORT_NUMBER);
            while (true) {
                Socket socket = listener.accept();
                try {
                    String dateString = new Date().toString();

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(dateString);
                    System.out.println(" -- " + dateString);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                } finally {
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
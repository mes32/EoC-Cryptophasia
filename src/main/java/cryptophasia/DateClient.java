/*
    DateClient.java

    Simple client class that accesses DateServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class DateClient {

    private static final int PORT_NUMBER = 9090;

    DateClient() {

        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.print("Server IP: ");
        String serverAddress = scan.next();

        System.out.print("Server Port: ");
        int serverPort = scan.nextInt();

        System.out.println();

        while (true) {

            System.out.print("> ");
            String message = scan.nextLine();

            try {
                // String serverAddress = JOptionPane.showInputDialog(
                //     "Enter IP Address of a machine that is\n" +
                //     "running the date service on port " + PORT_NUMBER + ":");
                Socket socket = new Socket(serverAddress, serverPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String answer = input.readLine();
                System.out.println(" -- " + answer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
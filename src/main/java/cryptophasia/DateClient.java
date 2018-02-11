/*
    DateClient.java

    Simple client class that accesses DateServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class DateClient {

    private static final int PORT_NUMBER = 9090;

    DateClient() {
        try {
            String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n" +
                "running the date service on port " + PORT_NUMBER + ":");
            Socket s = new Socket(serverAddress, PORT_NUMBER);
            BufferedReader input =
                new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            JOptionPane.showMessageDialog(null, answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
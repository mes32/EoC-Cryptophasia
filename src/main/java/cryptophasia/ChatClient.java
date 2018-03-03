/*
    ChatClient.java

    Client that sends messages to the ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    ChatClient(InetAddress serverAddress, int serverPortNumber) {
        connect(serverAddress, serverPortNumber);
        new ChatClientGUI(inputStream, outputStream);
    }

    ChatClient() {
        System.out.println();
        InetAddress serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        System.out.println();

        connect(serverAddress, serverPortNumber);
        new ChatClientGUI(inputStream, outputStream);
    }

    private void connect(InetAddress serverAddress, int serverPortNumber) {
        try {
            socket = new Socket(serverAddress, serverPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to connect with the server.");
            System.exit(1);
        }
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to obtain input stream from the server.");
            System.exit(1);
        }
        try {
            outputStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to obtain output stream from the server.");
            System.exit(1);
        }
    }

    private InetAddress serverAddressPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String addressString = scan.next();
        byte[] address = stringToAddress(addressString);

        InetAddress serverAddress = null;
        try {
            serverAddress = InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("ERROR: Unusable server IP address.");
            System.exit(1);
        }
        return serverAddress;
    }

    private int serverPortNumberPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server Port: ");
        int serverPortNumber = scan.nextInt();
        return serverPortNumber;
    }

    private byte[] stringToAddress(String string) {
        String[] digits = string.split("\\.");
        byte[] address = new byte[digits.length];
        for (int i = 0; i < digits.length; i++) {
            Integer current = new Integer(digits[i]);
            address[i] = current.byteValue();
        }
        return address;
    }
}
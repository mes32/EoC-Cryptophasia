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

    ChatClient(InetAddress serverAddress, int serverPortNumber) throws IOException {
        connect(serverAddress, serverPortNumber);
        ChatClientGUI gui = new ChatClientGUI(inputStream, outputStream);
        gui.run();
    }

    ChatClient() throws IOException {
        System.out.println();
        InetAddress serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        System.out.println();

        connect(serverAddress, serverPortNumber);
        ChatClientGUI gui = new ChatClientGUI(inputStream, outputStream);
        gui.run();
    }

    private void connect(InetAddress serverAddress, int serverPortNumber) throws IOException {
        socket = new Socket(serverAddress, serverPortNumber);
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new PrintWriter(socket.getOutputStream(), true);
    }

    private InetAddress serverAddressPrompt() throws UnknownHostException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String addressString = scan.next();

        byte[] address = stringToAddress(addressString);
        InetAddress serverAddress = null;
        serverAddress = InetAddress.getByAddress(address);
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
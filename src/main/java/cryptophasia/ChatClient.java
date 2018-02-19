/*
    ChatClient.java

    Client that sends messages to the ChatServer
 */

package cryptophasia;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ChatClient {

    private String userName;

    JFrame frame;
    JTextField textField;

    ChatClient() {
        Socket socket = connectWithServer();
        PrintWriter outputStream = setupOutputStream(socket);

        frame = new JFrame("Chat Client - Message Input (" + userName + ")");
        textField = new JTextField(40);

        textField.setEditable(true);
        frame.getContentPane().add(textField);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputStream.println(textField.getText());
                textField.setText("");
            }
        });

        // Scanner scan = new Scanner(System.in);
        // while (true) {
            
        //     System.out.print("> ");
        //     String message = scan.nextLine();
        //     outputStream.println(message);

        //     if (message.equals(".")) {
        //         break;
        //     }
        // }
    }

    private Socket connectWithServer() {
        System.out.println();
        String serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        userName = userNamePrompt();
        System.out.println();

        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPortNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(ChatServer.CLIENT_TOKEN);
            out.println(userName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to connect with the server.");
            System.exit(1);
        }
        return socket;
    }

    private PrintWriter setupOutputStream(Socket socket) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to obtain output stream from the server.");
            System.exit(1);
        }
        return outputStream;
    }

    private String serverAddressPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String serverAddress = scan.next();
        return serverAddress;
    }

    private int serverPortNumberPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server Port: ");
        int serverPortNumber = scan.nextInt();
        return serverPortNumber;
    }

    private String userNamePrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("username: ");
        String userName = scan.nextLine();
        return userName;
    }
}
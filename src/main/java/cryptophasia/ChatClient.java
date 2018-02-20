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
import javax.swing.text.*;

public class ChatClient {

    private String userName;

    JFrame frame;
    JTextArea messageDisplay;
    JTextField textField;

    ChatClient() {
        Socket socket = connectWithServer();
        BufferedReader inputStream = setupInputStream(socket);
        PrintWriter outputStream = setupOutputStream(socket);

        frame = new JFrame("Chat Client (" + userName + ")");
        messageDisplay = new JTextArea(14, 40);
        textField = new JTextField(40);

        messageDisplay.setEditable(false);
        messageDisplay.setLineWrap(true);
        textField.setEditable(true);
        frame.getContentPane().add(new JScrollPane(messageDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), "North");
        frame.getContentPane().add(textField, "South");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputStream.println(textField.getText());
                textField.setText("");
            }
        });

        while (true) {
            try {
                String message = inputStream.readLine();
                messageDisplay.append(message + "\n");
                messageDisplay.setCaretPosition(messageDisplay.getDocument().getLength());
            } catch (IOException e) {
                e.printStackTrace();
                messageDisplay.append("<IOException> Could not read message\n");
                messageDisplay.setCaretPosition(messageDisplay.getDocument().getLength());
            }
        }


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

    private BufferedReader setupInputStream(Socket socket) {
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatClient was unable to obtain input stream from the server.");
            System.exit(1);
        }
        return inputStream;
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
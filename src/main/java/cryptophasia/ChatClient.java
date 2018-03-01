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

    private JFrame frame = new JFrame("Chat Client");
    private JTextArea messageDisplay = new JTextArea(14, 40);
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();

    ChatClient() {
        System.out.println();
        String serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        System.out.println();
        startGUI(serverAddress, serverPortNumber);
    }

    ChatClient(String serverAddress, int serverPortNumber) {
        startGUI(serverAddress, serverPortNumber);
    }

    private void startGUI(String serverAddress, int serverPortNumber) {
        Socket socket = connectWithServer(serverAddress, serverPortNumber);
        BufferedReader inputStream = setupInputStream(socket);
        PrintWriter outputStream = setupOutputStream(socket);

        configMessageDisplay();
        configTextField(outputStream);
        configFrame();
        frame.setVisible(true);

        String message;
        try {
            message = inputStream.readLine();
            if (message.equals(ChatServer.SUBMITNAME)) {
                do {
                    userName = userNameDialog();
                    outputStream.println(userName);
                    message = inputStream.readLine();
                } while(!message.equals(ChatServer.NAMEACCEPT));
            }

            textField.setEditable(true);
            frame.setTitle("Chat Client - " + userName);

            while (true) {
                message = inputStream.readLine();
                soundIndicator.play();
                messageDisplay.append(message + "\n");
                messageDisplay.setCaretPosition(messageDisplay.getDocument().getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void configMessageDisplay() {
        messageDisplay.setEditable(false);
        messageDisplay.setLineWrap(true);
    }

    private void configTextField(PrintWriter out) {
        textField.setEditable(false);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private void configFrame() {
        JScrollPane scrollPane = new JScrollPane(messageDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(scrollPane, "North");
        frame.getContentPane().add(textField, "South");
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Socket connectWithServer(String serverAddress, int serverPortNumber) {
        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPortNumber);
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

    private String userNameDialog() {
        String userName = JOptionPane.showInputDialog(
            frame,
            "Enter username:",
            "Username Dialog",
            JOptionPane.PLAIN_MESSAGE);
        return userName;
    }
}
/*
    ChatClientGUI.java

    GUI interface for ChatClient
 */

package cryptophasia;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class ChatClientGUI {

    private BufferedReader inputStream;
    private PrintWriter outputStream;

    private String userName;
    private JFrame frame = new JFrame("Chat Client");

    private ChatMessagePane messagePane = new ChatMessagePane();
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();

    ChatClientGUI(BufferedReader inputStream, PrintWriter outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        configTextField(outputStream);
        configFrame();
        frame.setVisible(true);

        String message;
        try {
            message = inputStream.readLine();
            if (message.equals(ChatServer.SUBMITNAME)) {
                do {
                    userName = userNameDialog();
                    messagePane.setUserName(userName);
                    outputStream.println(userName);
                    message = inputStream.readLine();
                } while(!message.equals(ChatServer.NAMEACCEPT));
            }
            textField.setEditable(true);
            frame.setTitle("Chat Client - " + userName);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                message = inputStream.readLine();
                if (message == null) {
                    appendMessage(" + Server is down");
                    textField.setEditable(false);
                    break;
                }
                soundIndicator.play();
                appendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                appendMessage("IOException - received unreadable message from server");
            }
        }
    }

    private void appendMessage(String message) {
        messagePane.appendMessage(message);
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
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(messagePane, BorderLayout.CENTER);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
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

    private JTextPane messageDisplay = new JTextPane();
    private JScrollPane messageScroll = new JScrollPane(messageDisplay);
    private JScrollBar vertical = messageScroll.getVerticalScrollBar();
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();

    ChatClientGUI(BufferedReader inputStream, PrintWriter outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        configMessageScroll();
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
                appendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void appendMessage(String message) {
        Color black = Color.BLACK;
        Color gray = new Color(192, 192, 192);
        Color blue = new Color(17, 96, 156);
        Color green = new Color(69, 206, 131);

        if (message.startsWith(" + ")) {
            // Message is from the server

            putText(message + "\n", false, gray);
        } else if (message.startsWith(userName)) {
            // Message is from local user

            String[] tokens = message.split("\\:", 2);
            String name = tokens[0];
            String body = tokens[1];

            putText(" " + name + ": ", true, blue);
            putText(body + "\n", false, black);
        } else {
            // Message is from another user

            String[] tokens = message.split("\\:", 2);
            String name = tokens[0];
            String body = tokens[1];

            putText(" " + name + ": ", true, green);
            putText(body + "\n", false, black);
        }
    }

    private void putText(String text, boolean bold, Color color) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributes = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        attributes = styleContext.addAttribute(attributes, StyleConstants.FontFamily, "Lucida Console");
        attributes = styleContext.addAttribute(attributes, StyleConstants.FontSize, 14);
        attributes = styleContext.addAttribute(attributes, StyleConstants.Bold, bold);
        attributes = styleContext.addAttribute(attributes, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
        
        carretToEnd();
        messageDisplay.setEditable(true);

        messageDisplay.setCharacterAttributes(attributes, false);
        messageDisplay.replaceSelection(text);

        messageDisplay.setEditable(false);
        carretToEnd();
    }

    private void carretToEnd() {
        int len = messageDisplay.getDocument().getLength();
        messageDisplay.setCaretPosition(len);
    }

    private void configMessageScroll() {
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void configMessageDisplay() {
        //messageDisplay.setContentType(document.getDocumentType());
        messageDisplay.setEditable(false);
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
        frame.getContentPane().add(messageScroll, BorderLayout.CENTER);
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
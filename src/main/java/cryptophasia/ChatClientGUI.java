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

    //private ChatDocument document = new ChatDocument();
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
            //document.setUserName(userName);

            while (true) {
                message = inputStream.readLine();
                soundIndicator.play();
                //document.append(message);

                // TODO: document should be self updating and not require setText()
                //messageDisplay.setText(document.toString());

                // TODO: The following line isn't 100% reliable
                //messageScroll.getViewport().setViewPosition(new Point(0, messageDisplay.getDocument().getLength()));

                appendToPane(messageDisplay, message + "\n", Color.BLUE);

                //messageScroll.getViewport().setViewPosition(new Point(0, messageDisplay.getDocument().getLength()));

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        tp.setEditable(true);
        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);

        len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setEditable(false);
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
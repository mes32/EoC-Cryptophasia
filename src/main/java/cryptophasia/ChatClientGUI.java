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

    ChatClientGUI(BufferedReader inputStream, PrintWriter outputStream) throws IOException {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        configTextField(outputStream);
        configFrame();
        frame.setVisible(true);

        setUserName();
    }

    public void run() {
        textField.setEditable(true);
        while (true) {
            try {
                AbstractMessage message = AbstractMessage.parse(inputStream.readLine());
                if (message == null) {
                    appendMessage(new ServerNotificationMessage("WARNING: Message equals null. (server -> " + userName + ")"));
                    appendMessage(new ServerNotificationMessage("In ChatClientGUI loop, stopping loop for " + userName));
                    break;
                } else {
                    appendMessageAbstract(message);
                }
                soundIndicator.play();
            } catch (IOException e) {
                e.printStackTrace();
                appendMessage(new ServerNotificationMessage("WARNING: IOException in ChatClientGUI loop reading from stream. (" + userName + " -> server)"));
            }
        }
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

    private void setUserName() throws IOException {
        boolean accepted = false;
        do {
            userName = userNameDialog();
            SubmitUsernameMessage submitMessage = new SubmitUsernameMessage(userName);
            outputStream.println(submitMessage.transmit());

            AcceptUsernameMessage acceptMessage = AcceptUsernameMessage.parse(inputStream.readLine());
            accepted = acceptMessage.isAccepted();

            if (!accepted) {
                appendMessage(new ServerNotificationMessage("Username '" + userName + "' was rejected by the server"));
                appendMessage(new ServerNotificationMessage("Trying again"));
            }
        } while(!accepted);

        messagePane.setUserName(userName);
        frame.setTitle("Chat Client - " + userName);
    }

    private void appendMessageAbstract(AbstractMessage message) {
        if (message instanceof ServerNotificationMessage) {
            appendMessage((ServerNotificationMessage) message);
        } else if (message instanceof ChatMessage) {
            appendMessage((ChatMessage) message);
        }
    }

    private void appendMessage(ServerNotificationMessage message) {
        messagePane.appendMessage(message);
    }

    private void appendMessage(ChatMessage message) {
        messagePane.appendMessage(message);
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
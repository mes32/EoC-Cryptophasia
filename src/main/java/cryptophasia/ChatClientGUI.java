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

import cryptophasia.exception.*;
import cryptophasia.networking.*;
import cryptophasia.networking.transmission.*;

public class ChatClientGUI {

    private SocketIO socket;
    private String username;
    private JFrame frame = new JFrame("Chat Client");

    private ChatMessagePane messagePane = new ChatMessagePane();
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();

    ChatClientGUI(InetAddress address, int port) throws IOException {
        socket = new SocketIO(address, port);
        configFrame();
        frame.setVisible(true);
        setUserName();        
        configTextField(socket.getOutputStream());
        run();
    }

    public void run() {
        textField.setEditable(true);
        while (true) {
            try {
                AbstractMessage message = AbstractMessage.parse(socket.readLine());
                if (message == null) {
                    appendMessage(new ServerNotificationMessage("Server shutdown"));
                    textField.setEditable(false);
                    break;
                } else {
                    appendMessageAbstract(message);
                }
                soundIndicator.play();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                appendMessage(new ServerNotificationMessage("WARNING: IOException in ChatClientGUI loop reading from stream. (" + username + " -> server)"));
            } catch (MalformedTransmissionException malformedMessageException) {
                malformedMessageException.printStackTrace();
                appendMessage(new ServerNotificationMessage("WARNING: MalformedTransmissionException in ChatClientGUI loop reading from stream. (" + username + " -> server)"));
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
        while (true) {
            username = userNameDialog();
            RequestUsername request = new RequestUsername(username);
            socket.transmit(request);
            accepted = socket.isAccepted(request);
            if (accepted) {
                break;
            } else {
                appendMessage(new ServerNotificationMessage("Username '" + username + "' was rejected by the server"));
                appendMessage(new ServerNotificationMessage("Trying again"));
            }
        }

        messagePane.setUserName(username);
        frame.setTitle("Chat Client - " + username);
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
        String username;
        do {
            username = JOptionPane.showInputDialog(
                frame,
                "Enter username:",
                "Username Dialog",
                JOptionPane.PLAIN_MESSAGE);
        } while(username == null || username.equals(""));
        return username;
    }
}
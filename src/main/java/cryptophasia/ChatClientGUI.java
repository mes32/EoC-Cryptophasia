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
    private String userName;
    private JFrame frame = new JFrame("Chat Client");

    private ChatMessagePane messagePane = new ChatMessagePane();
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();

    ChatClientGUI(InetAddress serverAddress, int serverPortNumber) throws IOException {
        socket = new SocketIO(serverAddress, serverPortNumber);

        configTextField(socket.getOutputStream());
        configFrame();
        frame.setVisible(true);
        setUserName();        

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
                appendMessage(new ServerNotificationMessage("WARNING: IOException in ChatClientGUI loop reading from stream. (" + userName + " -> server)"));
            } catch (MalformedTransmissionException malformedMessageException) {
                malformedMessageException.printStackTrace();
                appendMessage(new ServerNotificationMessage("WARNING: MalformedTransmissionException in ChatClientGUI loop reading from stream. (" + userName + " -> server)"));
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
            do {
                userName = userNameDialog();
            } while(userName == null || userName.equals(""));

            RequestUsername request = new RequestUsername(userName);
            socket.transmit(request);
            accepted = request.accepted();

            try {
                AcceptUsernameMessage acceptMessage = AcceptUsernameMessage.parse(socket.readLine());
                accepted = acceptMessage.isAccepted();
            } catch (IOException | MalformedTransmissionException e) {
                accepted = false;
            }

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
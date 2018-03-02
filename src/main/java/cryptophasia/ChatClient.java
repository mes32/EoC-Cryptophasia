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
import javax.swing.text.html.*;

public class ChatClient {

    private String userName;

    private JFrame frame = new JFrame("Chat Client");
    private JTextPane messageDisplay = new JTextPane();
    private JScrollPane messageScroll = new JScrollPane(messageDisplay);
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

        configMessageScroll();
        configMessageDisplay();
        configTextField(outputStream);
        configFrame();
        frame.setVisible(true);

        HTMLDocument html = (HTMLDocument) messageDisplay.getDocument();
        Element bodyElement = html.getElement("body");
        String junk = "<body style=\"font-family: sans-serif; font-size: 12px\">";

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
                html.insertBeforeEnd(bodyElement, "<p>" + message + "</p>");

                junk += messageToHTML(message);

                HTMLEditorKit kit = new HTMLEditorKit(); 
                StringWriter writer = new StringWriter();
                kit.write(writer, html, 0, html.getLength());
                String s = writer.toString();

                messageDisplay.setText(junk);
            }
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private String messageToHTML(String message) {


        int i = message.indexOf(':');
        if (i == -1) {
            return "<font color=C0C0C0>" + message + "</font><br>";
        }
        String name = message.substring(0, i);
        String body = message.substring(i+2);

        if (name.equals(userName)) {
            return "<b><font color=11609C>&nbsp;&nbsp;" + name + ":</font></b> <font color=083251>" + body + "</font><br>";
        } else {
            return "<b><font color=45CE83>&nbsp;&nbsp;" + name + ":</font></b> " + body + "<br>";
        }      }

    private void configMessageScroll() {
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void configMessageDisplay() {
        messageDisplay.setContentType("text/html");
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
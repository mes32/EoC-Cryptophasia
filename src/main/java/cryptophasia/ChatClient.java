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


    //private HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
    //private HTMLDocument document = new HTMLDocument();
    private JTextPane messageDisplay = new JTextPane();
    private JScrollPane messageScroll = new JScrollPane(messageDisplay);
    private JScrollBar vertical = messageScroll.getVerticalScrollBar();
    private JTextField textField = new JTextField(40);
    private ChatAudioIndicator soundIndicator = new ChatAudioIndicator();
    private StringBuilder stringBuilder;

    private int length = 0;

    ChatClient() {
        System.out.println();
        InetAddress serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        System.out.println();
        startGUI(serverAddress, serverPortNumber);
    }

    ChatClient(InetAddress serverAddress, int serverPortNumber) {
        startGUI(serverAddress, serverPortNumber);
    }

    private void startGUI(InetAddress serverAddress, int serverPortNumber) {
        Socket socket = connectWithServer(serverAddress, serverPortNumber);
        BufferedReader inputStream = setupInputStream(socket);
        PrintWriter outputStream = setupOutputStream(socket);

        configMessageScroll();
        configMessageDisplay();
        configTextField(outputStream);
        configFrame();
        frame.setVisible(true);

        // Element[] roots = document.getRootElements();
        // Element body = null;
        // for(int i = 0; i < roots[0].getElementCount(); i++ ) {
        //     Element element = roots[0].getElement(i);
        //     if( element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
        //         body = element;
        //         break;
        //     }
        // }

        stringBuilder = new StringBuilder("<body style=\"font-family: sans-serif; font-size: 12px\">");

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
                //document.insertBeforeEnd(body, message + "<br>");

                messageToHTML(message, stringBuilder);

                messageDisplay.setText(stringBuilder.toString());

                // TODO: The following line isn't 100% reliable
                messageScroll.getViewport().setViewPosition(new Point(0, messageDisplay.getDocument().getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void messageToHTML(String message, StringBuilder stringBuilder) {

        length++;

        int i = message.indexOf(':');
        if (i == -1) {
            stringBuilder.append("<font color=C0C0C0>" + message + "</font><br>");
        } else {
            String name = message.substring(0, i);
            String body = message.substring(i+2);

            if (name.equals(userName)) {
                stringBuilder.append("<b><font color=11609C>&nbsp;&nbsp;" + name + ":</font></b> <font color=083251>" + body + "</font><br>");
            } else {
                stringBuilder.append("<b><font color=45CE83>&nbsp;&nbsp;" + name + ":</font></b> " + body + "<br>");
            }
        }
    }

    private void configMessageScroll() {
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void configMessageDisplay() {
        messageDisplay.setContentType("text/html");
        messageDisplay.setEditable(false);
        //htmlEditorKit.install(messageDisplay);
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

    private Socket connectWithServer(InetAddress serverAddress, int serverPortNumber) {
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

    private InetAddress serverAddressPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String addressString = scan.next();
        byte[] address = stringToAddress(addressString);

        InetAddress serverAddress = null;
        try {
            serverAddress = InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("ERROR: Unusable server IP address.");
            System.exit(1);
        }
        return serverAddress;
    }

    private byte[] stringToAddress(String string) {
        String[] digits = string.split("\\.");
        byte[] address = new byte[digits.length];
        for (int i = 0; i < digits.length; i++) {
            Integer current = new Integer(digits[i]);
            address[i] = current.byteValue();
        }
        return address;
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
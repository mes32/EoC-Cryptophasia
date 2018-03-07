/*
    ChatClientHandler.java

    A handler for ChatClient threads running on ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;

public class ChatClientHandler extends Thread {
    private Socket socket;
    private ChatServer server;
    private BufferedReader in;
    private PrintWriter out;

    private String userName;

    public ChatClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        server.display(new ServerNotificationMessage("New CLIENT found"));
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            server.display(new ServerNotificationMessage("WARNING: Unable to start I/O for new client"));
            server.display(new ServerNotificationMessage("In ChatClientHandler I/O setup, returning now"));
            return;
        }

        try {
            String transmission = in.readLine();
            if (SubmitUsernameMessage.indicated(transmission)) {
                SubmitUsernameMessage submission = SubmitUsernameMessage.parse(transmission);
                userName = submission.getUsername();
            }

            out.println(ChatServer.NAMEACCEPT);
            server.addWriter(out);
            server.display(new ServerNotificationMessage(userName + " joined chat server"));

        } catch (IOException e) {
            server.display(new ServerNotificationMessage("WARNING: New CLIENT was refused"));
            server.display(new ServerNotificationMessage("In ChatClientHandler username setup, returning now"));
            return;
        }

        String message;
        while (true) {
            try {
                message = in.readLine();
                if (message == null) {
                    server.display(new ServerNotificationMessage("WARNING: Message equals null. Sender is " + userName));
                    server.display(new ServerNotificationMessage("In ChatClientHandler loop, removing output stream and stopping loop for " + userName));
                    server.removeWriter(out);
                    break;
                } else {
                    server.display(new ChatMessage(userName, message));
                }
            } catch (IOException e) {
                server.display(new ServerNotificationMessage("WARNING: IOException in ChatClientHandler loop reading from stream. (server -> " + userName + ")"));
            }
        }
    }
}
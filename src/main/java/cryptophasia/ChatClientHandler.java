/*
    ChatClientHandler.java

    A handler for ChatClient threads running on ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;

import cryptophasia.exception.*;
import cryptophasia.networking.*;

public class ChatClientHandler extends Thread {
    
    private SocketIO socket;
    private ChatServer server;
    private String username;

    public ChatClientHandler(SocketIO socket, ChatServer server) throws IOException {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        usernameLoop();
        server.addWriter(socket.getOutputStream());
        server.display(new ServerNotificationMessage(username + " joined chat server"));
        mainLoop();
    }

    private void usernameLoop() {
        String transmission;
        boolean accepted = false;
        do {
            try {
                transmission = socket.readLine();
                SubmitUsernameMessage submission = SubmitUsernameMessage.parse(transmission);
                username = submission.getUsername();

                accepted = true;

                AcceptUsernameMessage acceptance = new AcceptUsernameMessage(accepted);
                socket.println(acceptance.transmit());
            } catch (IOException e) {
                server.display(new ServerNotificationMessage("WARNING: IOException in ChatClientHandler usernameLoop()"));
            } catch (MalformedMessageException e2) {
                server.display(new ServerNotificationMessage("WARNING: MalformedMessageException in ChatClientHandler usernameLoop()"));
            }
        } while (!accepted);
    }

    private void mainLoop() {
        String transmission;
        while (true) {
            try {
                transmission = socket.readLine();
                if (transmission == null) {
                    server.display(new ServerNotificationMessage(username + " left chat server"));
                    server.removeWriter(socket.getOutputStream());
                    break;
                } else {
                    // TODO: For now this assumes all incomming transmissions are chat messages
                    String message = transmission;
                    server.display(new ChatMessage(username, message));
                }
            } catch (IOException e) {
                server.display(new ServerNotificationMessage("WARNING: IOException in ChatClientHandler loop reading from stream. (server -> " + username + ")"));
            }
        }
    }
}
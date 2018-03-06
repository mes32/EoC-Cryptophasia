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
    private int number;

    public ChatClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            server.display(" + New CLIENT added");
            out.println(ChatServer.SUBMITNAME);
            userName = in.readLine();
            out.println(ChatServer.NAMEACCEPT);
            server.addWriter(out);
            server.display(" + " + userName + " joined chat server");

            String message;
            while (true) {
                try {
                    message = in.readLine();
                    if (message == null || message.equals(".")) {
                        server.display(" + " + userName + " left chat server");
                        server.removeWriter(out);
                        out.println(ChatServer.SHUTDOWN);
                        break;
                    }
                    server.display(userName + ": " + message);
                } catch (IOException e) {
                    server.display(" + ERROR: IOException reading from " + userName + ".");
                }
            }
        } catch (IOException e) {

        }
    }
}
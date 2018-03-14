/*
    ServerSocketIO.java

    A simple wrapper around ServerSocket. Functions as the server-side listener to for SocketIO
    connections.
 */

package cryptophasia.networking;

import java.io.*;
import java.net.*;

public class ServerSocketIO {

    private ServerSocket server;

    ServerSocketIO(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public SocketIO accept() throws IOException {
        Socket socket = server.accept();
        return new SocketIO(socket);
    }

    public void close() throws IOException {
        server.close();
    }
}
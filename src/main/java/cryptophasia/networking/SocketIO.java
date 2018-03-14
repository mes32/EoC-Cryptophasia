/*
    SocketIO.java

    Networking socket packaged with the associated I/O streams
 */

package cryptophasia.networking;

import java.io.*;
import java.net.*;

public class SocketIO {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SocketIO(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketIO(InetAddress address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public PrintWriter getOutputStream() {
        return out;
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void println(String line) { 
        out.println(line);
    }
}
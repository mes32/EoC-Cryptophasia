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

    public SocketIO(Socket socket) {
        this.socket = socket;
    }

    public String read() {
        return "";
    }

    public void write(String string) {
        
    }
}
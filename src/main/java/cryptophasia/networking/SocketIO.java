/*
    SocketIO.java

    Networking socket packaged with the associated I/O streams
 */

package cryptophasia.networking;

import java.io.*;
import java.net.*;

import cryptophasia.exception.*;
import cryptophasia.networking.transmission.*;

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

    public void transmit(CryptophasiaTransmission transmission) {
        out.println(transmission.encode());
    }

    public RequestUsername receive() throws IOException, MalformedTransmissionException {
        return RequestUsername.decode(in.readLine());
    }

    public PrintWriter getOutputStream() {
        // TODO: This method should not need to be exposed
        return out;
    }

    public String readLine() throws IOException {
        // TODO: This method should not need to be exposed
        return in.readLine();
    }

    public void println(String line) { 
        // TODO: This method should not need to be exposed
        out.println(line);
    }
}
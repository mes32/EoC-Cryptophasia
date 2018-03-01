/*
    ChatServer.java

    A terminal-based multi-user instant messaging server
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    public static final String SUBMITNAME = "SUBMITNAME";
    public static final String NAMEACCEPT = "NAMEACCEPT";

    private int userCount = 0;
    private ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
    private int portNumber;
    private ServerSocket listener;

    ChatServer() {
        String internalAddress = getInternalAddress();
        String externalAddress = getExternalAddress();
        portNumber = portNumberPrompt(internalAddress, externalAddress);
        listener = openConnection(portNumber);
    }

    public void run() {
        try {
            while (true) {
                try {
                    Socket socket = listener.accept();
                    display(" + Socket connection accepted");
                    new ClientHandler(socket, this).start();
                } catch (IOException e) {
                    display(" + Socket connection refused");
                }
            }
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void addWriter(PrintWriter writer) {
        printWriters.add(writer);
    }

    private String getInternalAddress() {
        try {
            InetAddress ipLocalHost = InetAddress.getLocalHost();
            return ipLocalHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return "###.###.###.###";
    }

    private String getExternalAddress() {
        // String externalInetAddress = ExternalAddressService.whatIsMyIp();
        // if (externalInetAddress == null) {
        //     externalInetAddress = "";
        // }

        return "###.###.###.###";
    }

    private int portNumberPrompt(String internalAddress, String externalAddress) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Local IP Address: " + internalAddress);
            System.out.println("External IP Address: " + externalAddress);
            System.out.print("Port Number: ");

            try {
                int portNumber = scan.nextInt();
                System.out.println();
                return portNumber;
            } catch (InputMismatchException e) {
                scan.nextLine();
            }
        }
    }

    private ServerSocket openConnection(int portNumber) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatServer was unable to open connection.");
            System.exit(1);
        }
        display(" + Server now listening on port: " + portNumber);
        return listener;
    }

    private void display(String message) {
        System.out.println(message);
        for (PrintWriter writer : printWriters) {
            writer.println(message);
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private ChatServer server;
        private BufferedReader in;
        private PrintWriter out;

        private String userName;
        private int number;

        public ClientHandler(Socket socket, ChatServer server) {
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
}
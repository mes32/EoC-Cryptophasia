/*
    ChatServer.java

    A terminal-based multi-user instant messaging server
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    public static final String MONITOR_TOKEN = "MONITOR";
    public static final String CLIENT_TOKEN = "CLIENT";

    private int userCount = 0;
    private ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();

    ChatServer() {
        String internalAddress = getInternalAddress();
        String externalAddress = getExternalAddress();
        int portNumber = portNumberPrompt(internalAddress, externalAddress);
        ServerSocket listener = openConnection(portNumber);

        try {
            while (true) {
                try {
                    Socket socket = listener.accept();
                    display(" + Socket connection accepted");
                    new SwitchHandler(socket, this).start();
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

    public int incrementUsers() {
        userCount++;
        return userCount;
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

    private static class SwitchHandler extends Thread {
        private static final String ANSI_RESET = "\u001B[0m";
        private static final String ANSI_RED = "\u001B[31m";
        private static final String ANSI_GREEN = "\u001B[32m";
        private static final String ANSI_BLUE = "\u001B[34m";

        private Socket socket;
        private ChatServer server;
        private BufferedReader in;
        private PrintWriter out;

        private String userName;
        private int number;

        public SwitchHandler(Socket socket, ChatServer server) {
            this.socket = socket;
            this.server = server;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String connectionType = in.readLine();
                if (connectionType.equals(MONITOR_TOKEN)) {
                    server.addWriter(out);
                    server.display(" + New MONITOR added");
                } else if (connectionType.equals(CLIENT_TOKEN)) {
                    server.display(" + New CLIENT added");
                    userName = in.readLine();
                    number = server.incrementUsers();
                    String colorCode = getColorCode(number);
                    server.display(" + " + colorCode + userName + ANSI_RESET + " joined chat server");
                    
                    String message = in.readLine();
                    while (message != null && !message.equals(".")) {
                        server.display(colorCode + userName + ANSI_RESET + ": " + message);
                        message = in.readLine();
                    }
                }
            } catch (IOException e) {

            }
        }

        private String getColorCode(int number) {
            if (number % 3 == 0) {
                return ANSI_GREEN;
            } else if (number % 3 == 1) {
                return ANSI_BLUE;
            } else if (number % 3 == 2) {
                return ANSI_RED;
            }
            return "";
        }
    }           
}
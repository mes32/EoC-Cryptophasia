/*
    DateServer.java

    Simple date server class
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;

public class DateServer {

    //private static final int PORT_NUMBER = 9090;

    DateServer() {

        // String externalInetAddress = ExternalAddressService.whatIsMyIp();
        // if (externalInetAddress == null) {
        //     externalInetAddress = "";
        // }

        Scanner scan = new Scanner(System.in);

        int portNumber = 0;
        String externalInetAddress = null;

        try {
            InetAddress ipLocalHost = InetAddress.getLocalHost();

            System.out.println();
            System.out.println("Local IP Address: " + ipLocalHost.getHostAddress());
            System.out.println("External IP Address: " + "###.###.###.###");
            System.out.print("Port Number: ");

            portNumber = scan.nextInt();

            System.out.println();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ServerSocket listener = new ServerSocket(portNumber);


            while (true) {
                Socket socket = listener.accept();
                try {
                    String dateString = new Date().toString();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String input = in.readLine();
                    System.out.println(" -- " + input);

                    System.out.print("> ");
                    String message = scan.nextLine();
                    out.println(message);


                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                } finally {
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
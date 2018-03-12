/*
    LaunchGame.java

    Launcher class for the game "Engines of Creation: Cryptophasia"
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class LaunchGame {
    private static final int SELECT_SERVER = 1;
    private static final int SELECT_CLIENT = 2;

    private static final String TEST_SERVER = "-server";
    private static final String TEST_CLIENT = "-client";
    private static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    private static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals(TEST_SERVER)) {
            try {
                ChatServer server = new ChatServer(DEFAULT_ADDRESS, DEFAULT_PORT);
                server.start();
                new ChatClient(DEFAULT_ADDRESS, DEFAULT_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else if (args.length == 1 && args[0].equals(TEST_CLIENT)) {
            try {
                new ChatClient(DEFAULT_ADDRESS, DEFAULT_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            showTitle();
            int choice = menuPrompt();
            try {
                startSelected(choice);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static void showTitle() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("+-----------------------------------+");
        System.out.println("| Engines of Creation: Cryptophasia |");
        System.out.println("+-----------------------------------+");
    }

    private static int menuPrompt() {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println();
            System.out.println("  1. Start server");
            System.out.println("  2. Connect w/ client");
            System.out.println();
            System.out.print("> ");

            try {
                choice = scan.nextInt();
            } catch (InputMismatchException e) {
                scan.nextLine();
            }

            if (choice == SELECT_SERVER || choice == SELECT_CLIENT) {
                return choice;
            }
        }
    }

    private static void startSelected(int choice) throws IOException {
        if (choice == SELECT_SERVER) {
            ChatServerConfiguration config = new ChatServerConfiguration();
            InetAddress address = config.getAddress();
            int port = config.getPortNumber();

            ChatServer server = new ChatServer(address, port);
            server.start();
            new ChatClient(address, port);
        } else if (choice == SELECT_CLIENT) {
            new ChatClient();
        }
    }
}

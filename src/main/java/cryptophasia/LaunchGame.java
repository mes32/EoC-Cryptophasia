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

    private static final String TEST_SERVER_FLAG = "-server";
    private static final String TEST_CLIENT_FLAG = "-client";
    private static final InetAddress DEFAULT_ADDRESS = InetAddress.getLoopbackAddress();
    private static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                menu();
            } else if (args[0].equals(TEST_SERVER_FLAG)) {
                startDefaultServer();
            } else if (args[0].equals(TEST_CLIENT_FLAG)) {
                startDefaultClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void menu() throws IOException {
        showTitle();
        int choice = menuPrompt();
        if (choice == SELECT_SERVER) {
            InetAddress address = InetAddress.getLoopbackAddress();
            int port = StartupPrompt.serverPortNumberPrompt();

            ChatServer server = new ChatServer(address, port);
            server.start();
            new ChatClientGUI(address, port);
        } else if (choice == SELECT_CLIENT) {
            System.out.println();
            InetAddress serverAddress = StartupPrompt.clientAddressPrompt();
            int serverPortNumber = StartupPrompt.clientPortNumberPrompt();
            System.out.println();

            new ChatClientGUI(serverAddress, serverPortNumber);
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

    private static void startDefaultServer() throws IOException {
        ChatServer server = new ChatServer(DEFAULT_ADDRESS, DEFAULT_PORT);
        server.start();
        new ChatClientGUI(DEFAULT_ADDRESS, DEFAULT_PORT);
    }

    private static void startDefaultClient() throws IOException {
        new ChatClientGUI(DEFAULT_ADDRESS, DEFAULT_PORT);
    }
}

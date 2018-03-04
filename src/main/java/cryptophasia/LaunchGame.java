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
    //private static final int SELECT_MONITOR = 2;
    private static final int SELECT_CLIENT = 2;

    public static void main(String[] args) {
        showTitle();
        int choice = menuPrompt();
        startSelected(choice);
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

    private static void startSelected(int choice) {
        if (choice == SELECT_SERVER) {
            ChatServer server = new ChatServer();
            InetAddress loopBack = InetAddress.getLoopbackAddress();
            int serverPortNumber = server.getPortNumber();

            Runnable r = new Runnable() {
                public void run() {
                    server.run();
                }
            };
            new Thread(r).start();

            // SwingUtilities.invokeLater(new Runnable() {
            //         public void run() {
            //             new ChatClient(loopBack, serverPortNumber);
            //         }
            //     });

            new ChatClient(loopBack, serverPortNumber);
        } else if (choice == SELECT_CLIENT) {
            new ChatClient();
        }
    }
}

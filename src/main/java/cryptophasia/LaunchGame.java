/*
    LaunchGame.java

    Launcher class for the game "Engines of Creation: Cryptophasia"
 */

package cryptophasia;

import java.io.*;
import java.util.*;

public class LaunchGame {
    private static final int SELECT_SERVER = 1;
    private static final int SELECT_MONITOR = 2;
    private static final int SELECT_CLIENT = 3;

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
            System.out.println("  2. Connect w/ monitor");
            System.out.println("  3. Connect w/ chat client");
            System.out.println();
            System.out.print("> ");

            try {
                choice = scan.nextInt();
            } catch (InputMismatchException e) {
                scan.nextLine();
            }

            if (choice == SELECT_SERVER || choice == SELECT_MONITOR || choice == SELECT_CLIENT) {
                return choice;
            }
        }
    }

    private static void startSelected(int choice) {
        if (choice == SELECT_SERVER) {
            new ChatServer();
        } else if (choice == SELECT_MONITOR) {
            new ChatMonitor();
        } else if (choice == SELECT_CLIENT) {
            new ChatClient();
        }
    }
}

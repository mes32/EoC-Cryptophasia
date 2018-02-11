/*
    LaunchGame.java

    Launcher class for the game "Engines of Creation: Cryptophasia"
 */

package cryptophasia;

import java.io.*;
import java.util.*;

public class LaunchGame {
    public static void main(String[] args) {

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("+-----------------------------------+");
        System.out.println("| Engines of Creation: Cryptophasia |");
        System.out.println("+-----------------------------------+");

        Scanner scan = new Scanner(System.in);
        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println();
            System.out.println("  1. Run as server");
            System.out.println("  2. Run as client");
            System.out.println();
            System.out.print("> ");

            try {
                choice = scan.nextInt();
            } catch (InputMismatchException e) {
                scan.nextLine();
            }

            if (choice == 1 || choice == 2) {
                validChoice = true;
                if (choice == 1) {
                    new DateServer();
                }
                if (choice == 2) {
                    new DateClient();
                }
            }
        }
    }
}
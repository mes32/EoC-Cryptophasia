/*
    ChatMonitor.java

    Displays a feed of messages from the ChatServer
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

public class ChatMonitor {

    private static final String AUDIO_INDICATOR_NAME = "/sound60.wav";

    private Clip audioIndicator;

    ChatMonitor() {
        Socket socket = connectWithServer();
        BufferedReader inputStream = setupInputStream(socket);
        while (true) {
            try {
                String message = inputStream.readLine();
                if (message == null) {
                    break;
                }
                playAudioIndicator();
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("ERROR: ChatMonitor was unable to read message from the server.");
                System.exit(1);
            }
        }
    }

    private void playAudioIndicator() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(AUDIO_INDICATOR_NAME); 
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            audioIndicator = (Clip) AudioSystem.getLine(info);
            audioIndicator.open(audioStream);
            audioIndicator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Socket connectWithServer() {
        System.out.println();
        String serverAddress = serverAddressPrompt();
        int serverPortNumber = serverPortNumberPrompt();
        System.out.println();

        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPortNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(ChatServer.MONITOR_TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatMonitor was unable to connect with the server.");
            System.exit(1);
        }
        return socket;
    }

    private BufferedReader setupInputStream(Socket socket) {
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: ChatMonitor was unable to obtain input stream from the server.");
            System.exit(1);
        }
        return inputStream;
    }

    private String serverAddressPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server IP: ");
        String serverAddress = scan.next();
        return serverAddress;
    }

    private int serverPortNumberPrompt() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Server Port: ");
        int serverPortNumber = scan.nextInt();
        return serverPortNumber;
    }
}
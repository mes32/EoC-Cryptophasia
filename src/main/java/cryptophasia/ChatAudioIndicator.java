/*
    ChatAudioIndicator.java

    Plays a sound when the ChatClient recieves a new message
 */

package cryptophasia;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

public class ChatAudioIndicator {

    private static final String AUDIO_INDICATOR_NAME = "/sound60.wav";

    private Clip clip;

    ChatAudioIndicator() {

    }

    public void play() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(AUDIO_INDICATOR_NAME); 
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
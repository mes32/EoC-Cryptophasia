/*
    ChatDocument.java

    Current body of chat messages used by ChatClient and ChatClientGUI
 */

package cryptophasia;

import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class ChatDocument extends HTMLDocument {

    private static final String GRAY_HEX = "C0C0C0";
    private static final String BLUE_HEX = "11609C";
    private static final String GREEN_HEX = "45CE83";

    private static final String BODY_STYLE = "<body style=\"font-family: sans-serif; font-size: 12px\">";
    private static final String FONT_SERVER = "<font color=" + GRAY_HEX + ">";
    private static final String FONT_SERVER_OFF = "</font>";
    private static final String FONT_SELF = "<b><font color=" + BLUE_HEX + ">&nbsp;";
    private static final String FONT_SELF_OFF = "</font></b>&nbsp;";
    private static final String FONT_OTHER = "<b><font color=" + GREEN_HEX + ">&nbsp;";
    private static final String FONT_OTHER_OFF = "</font></b>&nbsp;";

    private static final String documentType = "text/html";

    private String userName = "";
    private StringBuilder stringBuilder;
    private int length = 0;

    private Element root;

    ChatDocument() {
        super();
        stringBuilder = new StringBuilder(BODY_STYLE);

        root = createDefaultRoot();
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setUserName(String userName) {
        this.userName = userName + ":";
    }

    public void append(String message) {
        stringBuilder.append(messageToHTML(message));

        /*try {
            insertBeforeEnd(root, "<p>Test</p>");
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }*/
    }

    public String toString() {
        return stringBuilder.toString();
    }

    private String messageToHTML(String message) {
        length++;
        String html = "";
        int i = message.indexOf(':');
        if (i == -1) {
            return FONT_SERVER + message + FONT_SERVER_OFF + "<br>";
        }
        String name = message.substring(0, i+1);
        String body = message.substring(i+2);

        if (name.equals(userName)) {
            return FONT_SELF + name + FONT_SELF_OFF + body + "<br>";
        } else {
            return FONT_OTHER + name + FONT_OTHER_OFF + body + "<br>";
        }
    }
}
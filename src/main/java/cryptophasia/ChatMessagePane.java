/*
    ChatMessagePane.java

    GUI element displaying chat messages
 */

package cryptophasia;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class ChatMessagePane extends JScrollPane {

    private static final String FONT_FAMILY = "Lucida Console";
    private static final int FONT_SIZE = 14;
    private static final int FONT_ALIGNMENT = StyleConstants.ALIGN_LEFT;

    private static final Color BLACK = Color.BLACK;
    private static final Color GRAY = new Color(192, 192, 192);
    private static final Color BLUE = new Color(17, 96, 156);
    private static final Color GREEN = new Color(69, 206, 131);

    private static final AttributeSet DEFAULT_STYLE = ChatMessagePane.attributeFactory(false, BLACK);
    private static final AttributeSet SERVER_NOTE_STYLE = ChatMessagePane.attributeFactory(false, GRAY);
    private static final AttributeSet SELF_NAME_STYLE = ChatMessagePane.attributeFactory(true, BLUE);
    private static final AttributeSet OTHER_NAME_STYLE = ChatMessagePane.attributeFactory(true, GREEN);

    private final JTextPane messageDisplay = new JTextPane();

    private String userName;

    ChatMessagePane() {
        super();
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messageDisplay.setEditable(false);
        setViewportView(messageDisplay);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void appendMessage(String message) {
        if (message == null) {
            return;
        } else if (message.equals(ChatServer.SHUTDOWN)) {
            System.exit(1);
        } else if (message.startsWith(" + ")) {
            // Message is from the server

            putText(message + "\n", SERVER_NOTE_STYLE);
        } else if (message.indexOf(":") == -1) {
            // Message is of type error or unknown

            putText(message + "\n", DEFAULT_STYLE);
        } else {
            String[] tokens = message.split("\\:", 2);
            String name = tokens[0];
            String body = tokens[1];
            if (message.startsWith(userName)) {
                // Message is from local user

                putText(" " + name + ": ", SELF_NAME_STYLE);
                putText(body + "\n", DEFAULT_STYLE);
            } else {
                // Message is from another user

                putText(" " + name + ": ", OTHER_NAME_STYLE);
                putText(body + "\n", DEFAULT_STYLE);
            }
        }
    }

    public void appendMessage(AbstractMessage message) {

    }

    public void appendMessage(ServerNotificationMessage message) {
        putText(" + + " + message.getBody() + "\n", SERVER_NOTE_STYLE);
    }

    private void putText(String text, AttributeSet attributes) {        
        carretToBottom();
        messageDisplay.setEditable(true);

        messageDisplay.setCharacterAttributes(attributes, false);
        messageDisplay.replaceSelection(text);

        messageDisplay.setEditable(false);
        carretToBottom();
    }

    private void carretToBottom() {
        int len = messageDisplay.getDocument().getLength();
        messageDisplay.setCaretPosition(len);
    }

    private static AttributeSet attributeFactory(boolean bold, Color color) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributes = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        attributes = styleContext.addAttribute(attributes, StyleConstants.FontFamily, FONT_FAMILY);
        attributes = styleContext.addAttribute(attributes, StyleConstants.FontSize, FONT_SIZE);
        attributes = styleContext.addAttribute(attributes, StyleConstants.Bold, bold);
        attributes = styleContext.addAttribute(attributes, StyleConstants.Alignment, FONT_ALIGNMENT);
        return attributes;
    }
}
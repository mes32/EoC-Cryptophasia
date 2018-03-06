/*
    ChatMessage.java

    A message passing between clients on the chat channel
 */

package cryptophasia;

public class ChatMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.CHAT_MESSAGE;

    private String name;
    private String message;

    ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public static ChatMessage parse(String transmission) {
        int index = HEADER.length();
        String tail = transmission.substring(index);
        String[] tokens = tail.split("\\: ", 2);
        String name = tokens[0];
        String message = tokens[1];
        return new ChatMessage(name, message);
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String transmit() {
        return HEADER + name + ": " + message;
    }

    public String toString() {
        return name + ": " + message;
    }
}
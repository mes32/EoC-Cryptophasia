/*
    AbstractMessage.java

    A message sent between ChatClient and ChatServer
 */

package cryptophasia;

public abstract class AbstractMessage {

    protected static final String USER_NAME = "USER_NAME ";
    protected static final String SERVER_NOTE = "SERVER_NOTE ";
    protected static final String CHAT_MESSAGE = "CHAT_MESSAGE ";

    public static AbstractMessage parse(String text) {
        if (text == null) {
            return null;
        } else if (text.startsWith(CHAT_MESSAGE)) {
            return ChatMessage.parse(text);
        } else if (text.startsWith(SERVER_NOTE)) {
            return ServerNotificationMessage.parse(text);
        } else {
            return null;
        }
    }

    public abstract String transmit();
    public abstract String toString();
}
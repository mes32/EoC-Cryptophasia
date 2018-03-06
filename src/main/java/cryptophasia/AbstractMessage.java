/*
    AbstractMessage.java

    A message sent between ChatClient and ChatServer
 */

package cryptophasia;

public abstract class AbstractMessage {

    protected static final String SUBMITNAME = "NAME_ SUBMIT ";
    protected static final String NAMEACCEPT = "NAME_ACCEPT ";
    protected static final String SHUTDOWN = "SHUTDOWN ";
    protected static final String SERVER_NOTE = "SERVER_NOTE ";
    protected static final String CHAT_MESSAGE = "CHAT_MESSAGE ";

    public static AbstractMessage parse(String text) {
        if (text.startsWith(SERVER_NOTE)) {
            return ServerNotificationMessage.parse(text);
        } else if (text.startsWith(CHAT_MESSAGE)) {
            return ChatMessage.parse(text);
        }
        return null;
    }

    public abstract String transmit();
    public abstract String toString();
}
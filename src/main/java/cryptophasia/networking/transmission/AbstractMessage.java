/*
    AbstractMessage.java

    A message sent between ChatClient and ChatServer
 */

package cryptophasia.networking.transmission;

import cryptophasia.exception.*;

public abstract class AbstractMessage {
    
    protected static final String CHAT_MESSAGE = "CHAT_MESSAGE ";
    protected static final String SERVER_NOTE = "SERVER_NOTE ";
    protected static final String SUBMIT_USERNAME = "SUBMIT_USERNAME ";

    public static AbstractMessage parse(String text) throws MalformedTransmissionException {
        if (text == null) {
            return null;
        } else if (ChatMessage.indicated(text)) {
            return ChatMessage.parse(text);
        } else if (ServerNotificationMessage.indicated(text)) {
            return ServerNotificationMessage.parse(text);
        } else {
            throw new MalformedTransmissionException();
        }
    }

    public abstract String transmit();
    public abstract String toString();
}
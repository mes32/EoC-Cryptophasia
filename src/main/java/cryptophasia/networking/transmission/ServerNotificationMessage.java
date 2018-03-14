/*
    ServerNotificationMessage.java

    A notification relating to the server ChatServer
 */

package cryptophasia.networking.transmission;

import cryptophasia.exception.*;

public class ServerNotificationMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.SERVER_NOTE;

    private String body;

    public ServerNotificationMessage(String message) {
        body = message;
    }

    public static ServerNotificationMessage parse(String transmission) throws MalformedMessageException {
        try {
            int index = HEADER.length();
            String message = transmission.substring(index);
            return new ServerNotificationMessage(message);
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedMessageException("Could not parse ServerNotificationMessage.", e);
        }
    }

    public static boolean indicated(String transmission) {
        if (transmission.startsWith(HEADER)) {
            return true;
        } else {
            return false;
        }
    }

    public String getBody() {
        return body;
    }

    public String transmit() {
        return HEADER + body;
    }

    public String toString() {
        return " + " + body;
    }
}
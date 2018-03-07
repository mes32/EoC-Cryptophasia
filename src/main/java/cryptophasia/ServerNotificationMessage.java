/*
    ServerNotificationMessage.java

    A notification relating to the server ChatServer
 */

package cryptophasia;

public class ServerNotificationMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.SERVER_NOTE;

    private String body;

    ServerNotificationMessage(String message) {
        body = message;
    }

    public static ServerNotificationMessage parse(String transmission) {
        int index = HEADER.length();
        String message = transmission.substring(index);
        return new ServerNotificationMessage(message);
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
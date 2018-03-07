/*
    AcceptUsernameMessage.java

    A message where the server responds to a SubmitUsernameMessage indicating 
    whether the username is valid
 */

package cryptophasia;

public class AcceptUsernameMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.ACCEPT_USERNAME;

    private String username;

    AcceptUsernameMessage(String username) {
        this.username = username;
    }

    public static AcceptUsernameMessage parse(String transmission) {
        int index = HEADER.length();
        String username = transmission.substring(index);
        return new AcceptUsernameMessage(username);
    }

    public static boolean indicated(String transmission) {
        if (transmission.startsWith(HEADER)) {
            return true;
        } else {
            return false;
        }
    }

    public String transmit() {
        return HEADER + username;
    }

    public String toString() {
        return HEADER + username;
    }
}
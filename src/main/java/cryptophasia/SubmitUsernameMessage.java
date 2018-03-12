/*
    SubmitUsernameMessage.java

    A message where the client submits a desired username to the server
 */

package cryptophasia;

import cryptophasia.exception.*;

public class SubmitUsernameMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.SUBMIT_USERNAME;

    private String username;

    SubmitUsernameMessage(String username) {
        this.username = username;
    }

    public static SubmitUsernameMessage parse(String transmission) throws MalformedMessageException {
        try {
            int index = HEADER.length();
            String username = transmission.substring(index);
            return new SubmitUsernameMessage(username);
        } catch(IndexOutOfBoundsException e) {
            throw new MalformedMessageException("Could not parse SubmitUsernameMessage.", e);
        }
    }

    public static boolean indicated(String transmission) {
        if (transmission.startsWith(HEADER)) {
            return true;
        } else {
            return false;
        }
    }

    public String getUsername() {
        return username;
    }

    public String transmit() {
        return HEADER + username;
    }

    public String toString() {
        return HEADER + username;
    }
}
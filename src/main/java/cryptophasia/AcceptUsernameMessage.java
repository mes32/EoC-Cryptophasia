/*
    AcceptUsernameMessage.java

    A message where the server responds to a SubmitUsernameMessage indicating 
    whether the username is valid
 */

package cryptophasia;

import cryptophasia.exception.*;

public class AcceptUsernameMessage extends AbstractMessage {

    private static final String HEADER = AbstractMessage.ACCEPT_USERNAME;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private boolean accepted;

    AcceptUsernameMessage(boolean accepted) {
        this.accepted = accepted;
    }

    public static AcceptUsernameMessage parse(String transmission) throws MalformedMessageException {
        try {
            int index = HEADER.length();
            String accepted = transmission.substring(index);
            if (accepted.equals(TRUE)) {
                return new AcceptUsernameMessage(true);
            } else {
                return new AcceptUsernameMessage(false);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedMessageException("Could not parse AcceptUsernameMessage.", e);
        }
    }

    public static boolean indicated(String transmission) {
        if (transmission.startsWith(HEADER)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String transmit() {
        if (accepted) {
            return HEADER + TRUE;
        } else {
            return HEADER + FALSE;
        }
    }

    public String toString() {
        return transmit();
    }
}
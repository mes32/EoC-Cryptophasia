/*
    RequestUsername.java

    A message where the client submits a desired username to the server
 */

package cryptophasia.networking.transmission;

import cryptophasia.exception.*;

public class RequestUsername implements Request {

    private static final String HEADER = AbstractMessage.SUBMIT_USERNAME;

    private String username;
    private boolean accept = false;

    public RequestUsername(String username) {
        this.username = username;
    }

    public RequestUsername(String username, boolean accept) {
        this.username = username;
        this.accept = accept;
    }

    public static RequestUsername decode(String ecoded) throws MalformedTransmissionException {
        try {
            int index = HEADER.length();
            String tail = ecoded.substring(index);
            String[] tokens = tail.split(" ", 2);
            String acceptString = tokens[0];
            String username = tokens[1];

            boolean accept;
            if (acceptString.equals("1")) {
                accept = true;
            } else {
                accept = false;
            }

            return new RequestUsername(username, accept);
        } catch(IndexOutOfBoundsException e) {
            throw new MalformedTransmissionException("Could not parse RequestUsername transmission.", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public String encode() {
        String acceptString;
        if (accept) {
            acceptString = "1";
        } else {
            acceptString = "0";
        }
        return HEADER + acceptString + " " + username;
    }

    public String replyWithAccept(boolean accept) {
        this.accept = accept;
        return encode();
    }

    public boolean isAccepted(String reply) {
        try {
            RequestUsername requestReply = RequestUsername.decode(reply);
            if (requestReply.accept && this.equals(requestReply)) {
                return true;
            }
            return false;
        } catch (MalformedTransmissionException e) {
            return false;
        }
    }

    public boolean equals(RequestUsername other) {
        return username.equals(other.username);
    }
}
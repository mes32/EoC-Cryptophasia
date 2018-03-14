/*
    RequestUsername.java

    A message where the client submits a desired username to the server
 */

package cryptophasia.networking.transmission;

import cryptophasia.exception.*;

public class RequestUsername implements Request {

    private static final String HEADER = AbstractMessage.SUBMIT_USERNAME;

    private String username;

    public RequestUsername(String username) {
        this.username = username;
    }

    public static RequestUsername decode(String ecoded) throws MalformedTransmissionException {
        try {
            int index = HEADER.length();
            String username = ecoded.substring(index);
            return new RequestUsername(username);
        } catch(IndexOutOfBoundsException e) {
            throw new MalformedTransmissionException("Could not parse RequestUsername transmission.", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public String encode() {
        return HEADER + username;
    }

    public boolean accepted() {
        return true;
    }
}
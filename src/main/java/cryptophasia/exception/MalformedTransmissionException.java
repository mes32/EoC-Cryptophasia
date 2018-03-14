/*
    MalformedTransmissionException.java

    Exception indicating that a client-server message cannot be correctly parsed
 */

package cryptophasia.exception;

public class MalformedTransmissionException extends Exception {

    public MalformedTransmissionException() {
        super();
    }

    public MalformedTransmissionException(String message) {
        super(message);
    }

    public MalformedTransmissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedTransmissionException(Throwable cause) {
        super(cause);
    }
}
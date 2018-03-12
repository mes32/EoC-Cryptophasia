/*
    MalformedMessageException.java

    Exception indicating that a client-server message cannot be correctly parsed
 */

package cryptophasia.exception;

public class MalformedMessageException extends Exception {

    public MalformedMessageException() {
        super();
    }

    public MalformedMessageException(String message) {
        super(message);
    }

    public MalformedMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedMessageException(Throwable cause) {
        super(cause);
    }
}
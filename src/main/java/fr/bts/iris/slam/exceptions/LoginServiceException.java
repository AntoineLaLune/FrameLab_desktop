package fr.bts.iris.slam.exceptions;

public class LoginServiceException extends RuntimeException {
    public LoginServiceException(String message) {
        super(message);
    }
    public LoginServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

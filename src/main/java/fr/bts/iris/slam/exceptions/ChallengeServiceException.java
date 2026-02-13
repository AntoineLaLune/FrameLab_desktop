package fr.bts.iris.slam.exceptions;

public class ChallengeServiceException extends RuntimeException {
    public ChallengeServiceException(String message) {
        super(message);
    }

    public ChallengeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

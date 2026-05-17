package fr.bts.iris.slam.exceptions;

public class SubmitServiceException extends RuntimeException {
  public SubmitServiceException(String message) {
    super(message);
  }
  public SubmitServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

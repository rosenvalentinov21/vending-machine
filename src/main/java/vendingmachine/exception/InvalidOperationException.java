package vendingmachine.exception;

public class InvalidOperationException extends RuntimeException {

  public InvalidOperationException(final String message) {
    super(message);
  }
}

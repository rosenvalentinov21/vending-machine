package vendingmachine.exception;

public class InvalidCoinInputException extends RuntimeException {

  public InvalidCoinInputException(final String message) {
    super(message);
  }
}

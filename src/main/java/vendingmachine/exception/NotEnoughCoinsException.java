package vendingmachine.exception;

public class NotEnoughCoinsException extends RuntimeException {

  public NotEnoughCoinsException(final String message) {
    super(message);
  }

}

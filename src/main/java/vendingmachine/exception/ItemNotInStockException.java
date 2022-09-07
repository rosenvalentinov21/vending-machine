package vendingmachine.exception;

public class ItemNotInStockException extends RuntimeException {

  public ItemNotInStockException(final String message) {
    super(message);
  }
}

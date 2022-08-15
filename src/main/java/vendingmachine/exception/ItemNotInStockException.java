package vendingmachine.exception;

public class ItemNotInStockException extends RuntimeException{
    public ItemNotInStockException(String message) {
        super(message);
    }
}

package vendingmachine.exception;

public class InvalidCoinInputException extends RuntimeException{

    public InvalidCoinInputException(String message) {
        super(message);
    }
}

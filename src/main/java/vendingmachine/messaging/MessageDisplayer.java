package vendingmachine.messaging;

public class MessageDisplayer implements Messaging{

    @Override
    public void displayMessage(final String message) {
        System.out.println(message);
    }
}

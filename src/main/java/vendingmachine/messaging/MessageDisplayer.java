package vendingmachine.messaging;

public class MessageDisplayer implements Messaging{

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}

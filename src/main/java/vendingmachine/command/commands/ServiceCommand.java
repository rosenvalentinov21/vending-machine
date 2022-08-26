package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.command.Command;

public class ServiceCommand extends Command {

    public ServiceCommand(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void execute() {
        try {
            vendingMachine.service();
        } catch (InvalidOperationException e) {
            messageDisplayer.displayMessage(e.getMessage());
        }
    }
}

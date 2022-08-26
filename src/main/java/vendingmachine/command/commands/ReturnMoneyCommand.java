package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.command.Command;

public class ReturnMoneyCommand extends Command {

    public ReturnMoneyCommand(final VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void execute() {
        try {
            vendingMachine.returnMoney();
        } catch (InvalidOperationException e) {
            messageDisplayer.displayMessage(e.getMessage());
        }
    }
}

package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;

public class ExitCommand extends Command {

    public ExitCommand(final VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void execute() {
        System.exit(1);
    }
}

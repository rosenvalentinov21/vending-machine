package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;

public class ExitCommand extends Command {

    public ExitCommand(final VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    //TODO System.exit exits the JVM and does not allow the finally blocks to execute.Therefore, resources may remain open. Avoid using it.
    @Override
    public void execute() {
        System.exit(1);
    }
}

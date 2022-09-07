package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.command.ProceedResponse;

public class ExitCommand extends Command {

  public ExitCommand(final VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public ProceedResponse execute() {
    proceedResponse.setProceed(false);
    return proceedResponse;
  }
}

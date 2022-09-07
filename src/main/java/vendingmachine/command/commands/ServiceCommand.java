package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.command.ProceedResponse;
import vendingmachine.exception.InvalidOperationException;

public class ServiceCommand extends Command {

  public ServiceCommand(final VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public ProceedResponse execute() {
    try {
      vendingMachine.service();
    } catch (InvalidOperationException e) {
      messageDisplayer.displayMessage(e.getMessage());
    }
    return proceedResponse;
  }
}

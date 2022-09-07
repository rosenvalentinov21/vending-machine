package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.command.ProceedResponse;
import vendingmachine.exception.InvalidOperationException;

public class ReturnMoneyCommand extends Command {

  public ReturnMoneyCommand(final VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public ProceedResponse execute() {
    try {
      vendingMachine.returnMoney();
    } catch (InvalidOperationException e) {
      messageDisplayer.displayMessage(e.getMessage());
    }
    return proceedResponse;
  }
}

package vendingmachine.command.commands;

import java.math.BigDecimal;
import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.command.ProceedResponse;
import vendingmachine.view.dialog.VendingMachineDialog;

public class InsertCoinsCommand extends Command {

  private final VendingMachineDialog vendingMachineDialog;

  public InsertCoinsCommand(final VendingMachine vendingMachine,
      final VendingMachineDialog vendingMachineDialog) {
    super(vendingMachine);
    this.vendingMachineDialog = vendingMachineDialog;
  }

  @Override
  public ProceedResponse execute() {
    final BigDecimal amount = vendingMachineDialog.addCurrency();
    if (amount.compareTo(BigDecimal.ZERO) > 0) {
      vendingMachine.addCurrency(amount);
    } else if (vendingMachine.getClientMoney().equals(BigDecimal.ZERO)) {
      messageDisplayer.displayMessage(
          "There is no money in the bank, please insert before you proceed");
    }
    return proceedResponse;
  }
}

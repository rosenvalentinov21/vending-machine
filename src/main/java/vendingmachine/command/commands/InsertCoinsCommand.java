package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.view.dialog.VendingMachineDialog;

import java.math.BigDecimal;

public class InsertCoinsCommand extends Command {

    private final VendingMachineDialog vendingMachineDialog = new VendingMachineDialog(vendingMachine);

    public InsertCoinsCommand(final VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void execute() {
        final BigDecimal amount = vendingMachineDialog.addCurrency();
        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            vendingMachine.addCurrency(amount);
        } else {
            messageDisplayer.displayMessage("There is no money in the bank, please insert before you proceed");
        }
    }
}

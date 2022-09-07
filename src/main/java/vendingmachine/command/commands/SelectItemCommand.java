package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.command.ProceedResponse;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.view.dialog.VendingMachineDialog;

public class SelectItemCommand extends Command {

  private final VendingMachineDialog vendingMachineDialog;

  public SelectItemCommand(final VendingMachine vendingMachine,
      final VendingMachineDialog vendingMachineDialog) {
    super(vendingMachine);
    this.vendingMachineDialog = vendingMachineDialog;
  }

  @Override
  public ProceedResponse execute() {
    final ItemInventory itemInventory = vendingMachine.getItemInventory();
    if (!itemInventory.getAllItemTypes().isEmpty()) {
      selectItem(itemInventory);
    } else {
      messageDisplayer.displayMessage(
          "There are no items in the inventory, you may want to service the machine");
      vendingMachine.returnToInitialState();
    }
    return proceedResponse;
  }

  private void selectItem(final ItemInventory itemInventory) {
    final Item item = vendingMachineDialog.selectItem();
    if (itemInventory.isInStock(item)) {
      processSelectedItem(item);
    } else {
      messageDisplayer.displayMessage("The chosen item is not in stock, call service please.");
    }
  }

  private void processSelectedItem(final Item item) {
    vendingMachine.selectItem(item);
    vendingMachine.makeItem();
    vendingMachine.takeItem();
  }
}

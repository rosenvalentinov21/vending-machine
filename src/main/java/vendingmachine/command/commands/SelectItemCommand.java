package vendingmachine.command.commands;

import vendingmachine.VendingMachine;
import vendingmachine.command.Command;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.view.dialog.VendingMachineDialog;

public class SelectItemCommand extends Command {

    private final VendingMachineDialog vendingMachineDialog = new VendingMachineDialog(vendingMachine);

    public SelectItemCommand(final VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void execute() {
        final ItemInventory itemInventory = vendingMachine.getItemInventory();
        if (!itemInventory.getItemToQuantity().isEmpty()) {
            selectItem(itemInventory);
        } else {
            messageDisplayer.displayMessage("There are no items in the inventory, you may want to service the machine");
            vendingMachine.returnToInitialState();
        }

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

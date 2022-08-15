package vendingmachine.controller;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.service.ItemService;
import vendingmachine.view.command.Menu;
import vendingmachine.view.dialog.VendingMachineDialog;

import java.math.BigDecimal;
import java.util.List;

public class MachineController {
    private final VendingMachineDialog vendingMachineDialog = new VendingMachineDialog();
    private final ItemService itemService = new ItemService();
    private final MessageDisplayer messageDisplayer = new MessageDisplayer();

    public MachineController() {
        init();
    }

    private Menu menu;

    void init() {
        menu = new Menu("Vending machine menu", List.of(
                new Menu.Option("Insert Coins: ", () -> {
                    BigDecimal amount = vendingMachineDialog.addCurrency();
                    if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
                        VendingMachine.addCurrency(amount);
                    } else {
                        messageDisplayer.displayMessage("There is no money in the bank, please insert before you proceed");
                    }
                }),
                new Menu.Option("Select item: ", () -> {
                    if (!VendingMachine.itemInventory.itemToQuantity.isEmpty()) {
                        Item item = vendingMachineDialog.selectItem();
                        if (itemService.isInStock(item)) {
                            VendingMachine.selectItem(item);
                            VendingMachine.makeItem();
                            VendingMachine.takeItem();
                        } else {
                            messageDisplayer.displayMessage("The chosen item is not in stock, call service please.");
                        }
                    } else {
                        messageDisplayer.displayMessage("There are no items in the inventory, you may want to service the machine");
                        VendingMachine.returnToInitialState();
                    }
                }),
                new Menu.Option("Return money: ", () ->
                {
                    try {
                        VendingMachine.returnMoney();
                    } catch (InvalidOperationException e) {
                        messageDisplayer.displayMessage(e.getMessage());
                    }

                }),
                new Menu.Option("Service machine: ", () -> {
                    try {
                        VendingMachine.service();
                    } catch (InvalidOperationException e) {
                        messageDisplayer.displayMessage(e.getMessage());
                    }
                })
        ));
    }

    public void showMenu() {
        menu.show();
    }
}

package vendingmachine.controller;

import vendingmachine.VendingMachine;
import vendingmachine.command.Menu;
import vendingmachine.command.Option;
import vendingmachine.command.commands.*;
import vendingmachine.messaging.MessageDisplayer;

public class MachineController {
    private final VendingMachine vendingMachine = new VendingMachine();
    private final MessageDisplayer messageDisplayer = new MessageDisplayer();
    private final Menu menu = new Menu();

    public MachineController() {
        init();
    }

    void init() {
        menu.addOption(new Option("Insert coins ", new InsertCoinsCommand(vendingMachine)));
        menu.addOption(new Option("Select ", new SelectItemCommand(vendingMachine)));
        menu.addOption(new Option("Return money ", new ReturnMoneyCommand(vendingMachine)));
        menu.addOption(new Option("Service ", new ServiceCommand(vendingMachine)));
        menu.addOption(new Option("Exit menu  ", new ExitCommand(vendingMachine)));
    }

    public void showMenu() {
        while (true) {
            try {
                menu.showOptions();
                menu.executeOption(menu.chooseOption());
            } catch (Exception e) {
                messageDisplayer.displayMessage(e.getMessage());
            }

        }
    }
}

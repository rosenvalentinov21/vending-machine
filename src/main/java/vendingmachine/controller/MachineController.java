package vendingmachine.controller;

import vendingmachine.VendingMachine;
import vendingmachine.command.Menu;
import vendingmachine.command.Option;
import vendingmachine.command.ProceedResponse;
import vendingmachine.command.commands.ExitCommand;
import vendingmachine.command.commands.InsertCoinsCommand;
import vendingmachine.command.commands.ReturnMoneyCommand;
import vendingmachine.command.commands.SelectItemCommand;
import vendingmachine.command.commands.ServiceCommand;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.view.dialog.VendingMachineDialog;

public class MachineController {

  private final VendingMachine vendingMachine;
  private final MessageDisplayer messageDisplayer;
  private final VendingMachineDialog vendingMachineDialog;
  private final Menu menu;

  public MachineController(final VendingMachine vendingMachine,
      final MessageDisplayer messageDisplayer, VendingMachineDialog vendingMachineDialog,
      final Menu menu) {
    this.vendingMachine = vendingMachine;
    this.messageDisplayer = messageDisplayer;
    this.vendingMachineDialog = vendingMachineDialog;
    this.menu = menu;
    init();
  }

  void init() {
    menu.addOption(
        new Option("Insert coins ", new InsertCoinsCommand(vendingMachine, vendingMachineDialog)));
    menu.addOption(
        new Option("Select ", new SelectItemCommand(vendingMachine, vendingMachineDialog)));
    menu.addOption(new Option("Return money ", new ReturnMoneyCommand(vendingMachine)));
    menu.addOption(new Option("Service ", new ServiceCommand(vendingMachine)));
    menu.addOption(new Option("Exit menu  ", new ExitCommand(vendingMachine)));
  }

  public void showMenu() {
    Option option;
    ProceedResponse proceedResponse;
    while (true) {
      try {
        menu.showOptions();
        option = menu.chooseOption();
        proceedResponse = menu.executeOption(option);
        if (!proceedResponse.isProceed()) {
          return;
        }
      } catch (Exception e) {
        messageDisplayer.displayMessage(e.getMessage());
      }

    }
  }
}

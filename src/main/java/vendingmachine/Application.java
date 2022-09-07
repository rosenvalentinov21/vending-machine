package vendingmachine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import vendingmachine.command.Menu;
import vendingmachine.controller.MachineController;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.messaging.Reader;
import vendingmachine.state.States;
import vendingmachine.view.dialog.VendingMachineDialog;

public class Application {

  public static void main(String[] args) {

    final ItemInventory itemInventory = new ItemInventory(new HashMap<>());
    final CoinInventory coinInventory = new CoinInventory(new HashMap<>());

    final MessageDisplayer messageDisplayer = new MessageDisplayer();
    final Scanner scanner = new Scanner(System.in);
    final Reader reader = new Reader(scanner);

    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO, itemInventory, coinInventory);
    vendingMachine.getCoinInventory().refillCoinInventory();
    vendingMachine.getItemInventory().refillItemInventory();

    final VendingMachineDialog vendingMachineDialog = new VendingMachineDialog(messageDisplayer,
        reader);

    final Menu menu = new Menu(messageDisplayer, reader);
    final MachineController machineController = new MachineController(vendingMachine,
        messageDisplayer, vendingMachineDialog, menu);

    machineController.showMenu();
  }
}

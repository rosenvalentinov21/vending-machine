package vendingmachine.command.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;
import vendingmachine.view.dialog.VendingMachineDialog;

class SelectItemCommandTest {

  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()));

  private final VendingMachineDialog vendingMachineDialog = Mockito.mock(
      VendingMachineDialog.class);

  private final SelectItemCommand selectItemCommand = new SelectItemCommand(vendingMachine,
      vendingMachineDialog);

  @Test
  void execute() {
    vendingMachine.service();
    final BigDecimal BALANCE = BigDecimal.TEN;
    final Item EXPECTED_ITEM = Item.LATTE;
    vendingMachine.setClientMoney(BALANCE);
    vendingMachine.setBalance(BALANCE);

    when(vendingMachineDialog.selectItem()).thenReturn(EXPECTED_ITEM);

    vendingMachine.setState(States.SELECT);
    selectItemCommand.execute();

    assertNull(vendingMachine.getCurrentItem());
    assertEquals(vendingMachine.getClientMoney().doubleValue(), BigDecimal.ZERO.doubleValue());
    assertEquals(vendingMachine.getBalance().doubleValue(),
        BALANCE.add(EXPECTED_ITEM.price).doubleValue());
  }


  @Test
  void executeWithEmptyInventory_ShouldReturnToInitialState() {
    final Item EXPECTED_ITEM = Item.LATTE;
    when(vendingMachineDialog.selectItem()).thenReturn(EXPECTED_ITEM);

    vendingMachine.setState(States.SELECT);
    selectItemCommand.execute();

    assertEquals(vendingMachine.getState(), States.WAITING);
  }
}
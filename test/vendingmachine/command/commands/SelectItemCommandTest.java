package vendingmachine.command.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;
import vendingmachine.view.dialog.VendingMachineDialog;

class SelectItemCommandTest {

  private final PaymentService paymentService = mock(PaymentService.class);
  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()), paymentService);

  private final VendingMachineDialog vendingMachineDialog = Mockito.mock(
      VendingMachineDialog.class);

  private final SelectItemCommand selectItemCommand = new SelectItemCommand(vendingMachine,
      vendingMachineDialog);

  @Test
  void execute() {
    vendingMachine.service();
    vendingMachine.setState(States.SELECT);

    final Item EXPECTED_ITEM = Item.LATTE;
    when(vendingMachineDialog.selectItem()).thenReturn(EXPECTED_ITEM);
    final boolean EXPECTED_RESPONSE = true;
    when(paymentService.clientCanAffordItem(any(), any())).thenReturn(EXPECTED_RESPONSE);
    when(paymentService.clientBalanceWithSubtractedAmount(any(), any())).thenReturn(
        BigDecimal.ZERO);

    selectItemCommand.execute();

    assertNull(vendingMachine.getCurrentItem());
    assertEquals(vendingMachine.getClientMoney().doubleValue(), BigDecimal.ZERO.doubleValue());
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
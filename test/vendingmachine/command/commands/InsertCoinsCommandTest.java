package vendingmachine.command.commands;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;
import vendingmachine.view.dialog.VendingMachineDialog;

class InsertCoinsCommandTest {


  private final VendingMachineDialog vendingMachineDialog = mock(VendingMachineDialog.class);
  private final PaymentService paymentService = mock(PaymentService.class);

  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()), paymentService);

  private final InsertCoinsCommand insertCoinsCommand = new InsertCoinsCommand(vendingMachine,
      vendingMachineDialog);

  @Test
  void execute_WhenPositiveAmount_ShouldAddCurrency() {
    final BigDecimal INPUT = BigDecimal.ONE;
    when(vendingMachineDialog.addCurrency()).thenReturn(INPUT);

    insertCoinsCommand.execute();

    verify(paymentService).addCurrencyToMachine(any(), any());
  }

  @Test
  void execute_WhenAmountNotPositive_ShouldNotAddCurrency() {
    final BigDecimal INPUT = BigDecimal.ZERO;
    when(vendingMachineDialog.addCurrency()).thenReturn(INPUT);

    insertCoinsCommand.execute();

    verify(paymentService, times(0)).addCurrencyToMachine(any(), any());
  }

}
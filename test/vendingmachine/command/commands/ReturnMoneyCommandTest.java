package vendingmachine.command.commands;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;

class ReturnMoneyCommandTest {

  private final PaymentService paymentService = mock(PaymentService.class);

  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()), paymentService);

  private final ReturnMoneyCommand returnMoneyCommand = new ReturnMoneyCommand(vendingMachine);

  @Test
  void execute() {
    returnMoneyCommand.execute();
    verify(paymentService).returnClientMoney(any());
  }
}
package vendingmachine.command.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;

class ServiceCommandTest {

  private final PaymentService paymentService = mock(PaymentService.class);
  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()), paymentService);

  private final ServiceCommand serviceCommand = new ServiceCommand(vendingMachine);

  @Test
  void execute() {
    final int EXPECTED_INITIAL_LIST_SIZE = 0;
    assertEquals(vendingMachine.getItemInventory().getAllItemTypes().size(),
        EXPECTED_INITIAL_LIST_SIZE);

    serviceCommand.execute();

    assertTrue(
        vendingMachine.getItemInventory().getAllItemTypes().size() > EXPECTED_INITIAL_LIST_SIZE);
  }

}

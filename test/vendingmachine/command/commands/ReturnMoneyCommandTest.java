package vendingmachine.command.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

class ReturnMoneyCommandTest {

  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()));

  private final ReturnMoneyCommand returnMoneyCommand = new ReturnMoneyCommand(vendingMachine);

  @Test
  void execute() {
    vendingMachine.setClientMoney(BigDecimal.ONE);
    assertEquals(vendingMachine.getClientMoney(), BigDecimal.ONE);

    vendingMachine.service();
    vendingMachine.setState(States.SELECT);

    returnMoneyCommand.execute();
    assertEquals(vendingMachine.getClientMoney(), BigDecimal.ZERO);
  }
}
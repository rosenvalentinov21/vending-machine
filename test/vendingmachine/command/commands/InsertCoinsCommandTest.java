package vendingmachine.command.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;
import vendingmachine.view.dialog.VendingMachineDialog;

class InsertCoinsCommandTest {


  private final VendingMachineDialog vendingMachineDialog = mock(VendingMachineDialog.class);

  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING,
      BigDecimal.ZERO, BigDecimal.ZERO, new ItemInventory(new HashMap<>()),
      new CoinInventory(new HashMap<>()));

  @InjectMocks
  private final InsertCoinsCommand insertCoinsCommand = new InsertCoinsCommand(vendingMachine,
      vendingMachineDialog);

  @Test
  void execute() {
    when(vendingMachineDialog.addCurrency()).thenReturn(BigDecimal.ONE);

    insertCoinsCommand.execute();
    assertEquals(vendingMachine.getClientMoney(), BigDecimal.ONE);
  }
}
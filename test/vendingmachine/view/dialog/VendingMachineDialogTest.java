package vendingmachine.view.dialog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.messaging.Reader;

class VendingMachineDialogTest {

  private final Reader reader = mock(Reader.class);

  private final MessageDisplayer messageDisplayer = new MessageDisplayer();

  private final VendingMachineDialog vendingMachineDialog = new VendingMachineDialog(
      messageDisplayer, reader);

  @Test
  void addCurrency() {
    when(reader.readNextLine()).thenReturn(String.valueOf(BigDecimal.ONE),
        String.valueOf(Coin.values().length + 1));

    final var returnedAmount = vendingMachineDialog.addCurrency();
    final var EXPECTED_AMOUNT = Arrays.stream(Coin.values())
        .filter(coin -> coin.order == BigDecimal.ONE.intValue()).findAny()
        .orElseThrow(NoSuchElementException::new);
    assertEquals(returnedAmount, EXPECTED_AMOUNT.value);
  }

  @Test
  void selectItem() {
    when(reader.readNextInteger()).thenReturn(BigDecimal.ONE.intValue(),
        Item.values().length + 1);

    final var selectedItem = vendingMachineDialog.selectItem();
    final var EXPECTED_ITEM = Arrays.stream(Item.values())
        .filter(item -> item.order == BigDecimal.ONE.intValue()).findAny()
        .orElseThrow(NoSuchElementException::new);

    assertEquals(selectedItem.price, EXPECTED_ITEM.price);
    assertEquals(selectedItem.order, EXPECTED_ITEM.order);
  }
}
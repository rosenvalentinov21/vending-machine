package vendingmachine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

class PaymentServiceTest {

  private final PaymentService paymentService = new PaymentService();

  private final ItemInventory itemInventory = mock(ItemInventory.class);
  private final CoinInventory coinInventory = new CoinInventory(new HashMap<>());

  @Test
  void addCurrencyToMachine_ShouldAddToMachineBalance() {
    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO, itemInventory, coinInventory, paymentService);
    final BigDecimal EXPECTED_CURRENCY = BigDecimal.TEN;

    paymentService.addCurrencyToMachine(EXPECTED_CURRENCY, vendingMachine);

    assertEquals(vendingMachine.getBalance(), EXPECTED_CURRENCY);
  }

  @Test
  void addCurrencyToMachine_ShouldAddToClientBalance() {
    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO, itemInventory, coinInventory, paymentService);
    final BigDecimal EXPECTED_CURRENCY = BigDecimal.TEN;

    paymentService.addCurrencyToMachine(EXPECTED_CURRENCY, vendingMachine);

    assertEquals(vendingMachine.getClientMoney(), EXPECTED_CURRENCY);
  }

  @Test
  void returnClientMoney_ShouldEmptyClientBalance() {
    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO, itemInventory, coinInventory, paymentService);
    final BigDecimal INPUT = BigDecimal.TEN;
    final BigDecimal EXPECTED_BALANCE = BigDecimal.ZERO;

    vendingMachine.setClientMoney(INPUT);
    vendingMachine.getCoinInventory().refillCoinInventory();

    paymentService.returnClientMoney(vendingMachine);

    assertEquals(vendingMachine.getClientMoney(), EXPECTED_BALANCE);
  }

  @Test
  void clientCanAffordItem_ShouldReturnTrue() {
    final Item item = Item.ESPRESSO;
    final BigDecimal BALANCE = BigDecimal.TEN;

    final boolean result = paymentService.clientCanAffordItem(item.price, BALANCE);
    assertTrue(result);
  }

  @Test
  void clientCanAffordItem_ShouldReturnFalse() {
    final Item item = Item.ESPRESSO;
    final BigDecimal BALANCE = BigDecimal.ZERO;

    final boolean result = paymentService.clientCanAffordItem(item.price, BALANCE);
    assertFalse(result);
  }

  @Test
  void balanceWithNewAmount_ShouldReturnAddedAmounts() {
    final BigDecimal BALANCE = BigDecimal.TEN;
    final BigDecimal AMOUNT = BigDecimal.ONE;
    final BigDecimal EXPECTED_RESULT = BALANCE.add(AMOUNT);

    final BigDecimal actualResult = paymentService.balanceWithNewAmount(AMOUNT, BALANCE);
    assertEquals(actualResult, EXPECTED_RESULT);
  }

  @Test
  void clientBalanceWithNewAmount_ShouldReturnAddedAmounts() {
    final BigDecimal BALANCE = BigDecimal.TEN;
    final BigDecimal AMOUNT = BigDecimal.ONE;
    final BigDecimal EXPECTED_RESULT = BALANCE.add(AMOUNT);

    final BigDecimal actualResult = paymentService.clientBalanceWithNewAmount(AMOUNT, BALANCE);
    assertEquals(actualResult, EXPECTED_RESULT);
  }

  @Test
  void clientBalanceWithSubtractedAmount_ShouldReturnSubtractedAmounts() {
    final BigDecimal BALANCE = BigDecimal.TEN;
    final BigDecimal AMOUNT = BigDecimal.ONE;
    final BigDecimal EXPECTED_RESULT = BALANCE.subtract(AMOUNT);

    final BigDecimal actualResult = paymentService.clientBalanceWithSubtractedAmount(AMOUNT,
        BALANCE);
    assertEquals(actualResult, EXPECTED_RESULT);
  }

  @Test
  void calculateMachineBalance_ShouldCalculateBalance() {
    paymentService.calculateMachineBalance(coinInventory);
    final BigDecimal result = paymentService.calculateMachineBalance(coinInventory);

    assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
  }

}
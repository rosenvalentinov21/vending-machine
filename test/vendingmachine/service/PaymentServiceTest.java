package vendingmachine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

class PaymentServiceTest {

  final ItemInventory itemInventory = new ItemInventory(new HashMap<>());
  final CoinInventory coinInventory = new CoinInventory(new HashMap<>());

  @Test
  void getChange() {
    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO,
        itemInventory, coinInventory);
    final PaymentService paymentService = new PaymentService(vendingMachine);

    vendingMachine.getCoinInventory().refillCoinInventory();
    final BigDecimal EXPECTED_CHANGE = Coin.TWO_DOLLARS.value;
    List<Coin> change = paymentService.getChange(EXPECTED_CHANGE);

    assertEquals(change.get(0).value.doubleValue(), EXPECTED_CHANGE.doubleValue());
  }

  @Test
  void calculateMachineBalance() {
    final Map<Coin, Integer> coinTypeToQuantity = new HashMap<>();
    final int EXPECTED_BALANCE = 100;
    coinTypeToQuantity.put(Coin.DOLLAR, EXPECTED_BALANCE);
    final CoinInventory coinInventory = new CoinInventory(coinTypeToQuantity);
    final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
        BigDecimal.ZERO, itemInventory,
        coinInventory);

    final BigDecimal EXPECTED_INITIAL_BALANCE = BigDecimal.ZERO;
    assertEquals(EXPECTED_INITIAL_BALANCE.doubleValue(), vendingMachine.getBalance().doubleValue());

    final PaymentService paymentService = new PaymentService(vendingMachine);
    paymentService.calculateMachineBalance();
    assertEquals(EXPECTED_BALANCE, vendingMachine.getBalance().intValue());
  }
}
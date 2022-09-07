package vendingmachine.inventory.coin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoinInventoryTest {

  private Map<Coin, Integer> coinTypeToQuantity;

  @BeforeEach
  void reinitializeHashMap() {
    coinTypeToQuantity = new HashMap<>();
  }

  @Test
  void removeCoinFromInventory() {
    final int NUMBER_OF_DOLLARS = 1;
    coinTypeToQuantity.put(Coin.DOLLAR, NUMBER_OF_DOLLARS);
    final CoinInventory coinInventory = new CoinInventory(coinTypeToQuantity);

    assertEquals(coinInventory.calculateBalance().doubleValue(), BigDecimal.ONE.doubleValue());
    coinInventory.removeCoinFromInventory(Coin.DOLLAR);
    assertEquals(coinInventory.calculateBalance().doubleValue(), BigDecimal.ZERO.doubleValue());
  }

  @Test
  void refillCoinInventory() {
    final CoinInventory coinInventory = new CoinInventory(coinTypeToQuantity);

    assertEquals(coinInventory.calculateBalance().doubleValue(), BigDecimal.ZERO.doubleValue());

    coinInventory.refillCoinInventory();
    assertTrue(coinInventory.calculateBalance().doubleValue() > BigDecimal.ZERO.doubleValue());
  }

  @Test
  void calculateBalance() {
    final int NUMBER_OF_DOLLARS = 100;
    coinTypeToQuantity.put(Coin.DOLLAR, NUMBER_OF_DOLLARS);
    final CoinInventory coinInventory = new CoinInventory(coinTypeToQuantity);

    assertEquals(coinInventory.calculateBalance().doubleValue(),
        BigDecimal.valueOf(NUMBER_OF_DOLLARS).doubleValue());
  }

}
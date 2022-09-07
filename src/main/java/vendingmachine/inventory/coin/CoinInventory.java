package vendingmachine.inventory.coin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import vendingmachine.exception.NotEnoughCoinsException;

public class CoinInventory {

  private final Map<Coin, Integer> coinTypeToQuantity;

  public CoinInventory(final Map<Coin, Integer> coinTypeToQuantity) {
    this.coinTypeToQuantity = coinTypeToQuantity;
  }

  public void removeCoinFromInventory(final Coin coin) {
    final Integer quantity = coinTypeToQuantity.get(coin);

    if (quantity > 0) {
      coinTypeToQuantity.put(coin, quantity - 1);
    } else {
      throw new NotEnoughCoinsException("Coin inventory for type " + coin.value + " is empty!");
    }
  }


  public boolean hasCoin(final Coin coin) {
    return coinTypeToQuantity.containsKey(coin);
  }

  public BigDecimal calculateBalance() {
    final List<Coin> coinTypes = coinTypeToQuantity.keySet().stream().toList();

    return aggregateCoinValues(coinTypes);
  }

  private BigDecimal aggregateCoinValues(final List<Coin> coinTypes) {
    BigDecimal balance = BigDecimal.ZERO;
    for (final Coin coin : coinTypes) {
      final Integer count = coinTypeToQuantity.get(coin);
      balance = balance.add(coin.value.multiply(BigDecimal.valueOf(count)));
    }
    return balance;
  }

  public void refillCoinInventory() {
    coinTypeToQuantity.put(Coin.FIVE_CENTS, 50);
    coinTypeToQuantity.put(Coin.TEN_CENTS, 40);
    coinTypeToQuantity.put(Coin.QUARTER, 35);
    coinTypeToQuantity.put(Coin.FIFTY_CENTS, 30);
    coinTypeToQuantity.put(Coin.DOLLAR, 25);
    coinTypeToQuantity.put(Coin.TWO_DOLLARS, 20);
  }
}

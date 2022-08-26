package vendingmachine.inventory.coin;

import vendingmachine.exception.NotEnoughCoinsException;

import java.util.Map;

public class CoinInventory {

    private final Map<Coin, Integer> coinTypeToQuantity;

    public CoinInventory(final Map<Coin, Integer> coinTypeToQuantity) {
        this.coinTypeToQuantity = coinTypeToQuantity;
    }

    public void removeCoinFromInventory(final Coin coin) {
        Integer quantity = coinTypeToQuantity.get(coin);

        if (quantity > 0) {
            coinTypeToQuantity.put(coin, quantity - 1);
        } else throw new NotEnoughCoinsException("Coin inventory for type " + coin.value + " is empty!");
    }

    public boolean hasCoin(final Coin coin) {
        return coinTypeToQuantity.containsKey(coin);
    }

    public Map<Coin, Integer> getCoinTypeToQuantity() {
        return coinTypeToQuantity;
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

package vendingmachine.inventory.coin;

import vendingmachine.exception.NotEnoughCoinsException;

import java.util.Map;

public class CoinInventory {

    public Map<Coin, Integer> coinTypeToQuantity;

    public CoinInventory(Map<Coin, Integer> coinTypeToQuantity) {
        this.coinTypeToQuantity = coinTypeToQuantity;
    }

    public void getCoin(Coin coin) {
        Integer quantity = coinTypeToQuantity.get(coin);

        if (quantity > 0) {
            coinTypeToQuantity.put(coin, quantity - 1);
        } else throw new NotEnoughCoinsException("Coin inventory for type " + coin.value + " is empty!");
    }

    public boolean hasCoin(Coin coin) {
        return coinTypeToQuantity.containsKey(coin);
    }

    public Map<Coin, Integer> getCoinTypeToQuantity() {
        return coinTypeToQuantity;
    }
}

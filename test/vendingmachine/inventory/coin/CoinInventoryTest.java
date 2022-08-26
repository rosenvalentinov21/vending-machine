package vendingmachine.inventory.coin;

import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CoinInventoryTest {

    private final VendingMachine vendingMachine = new VendingMachine();

    @Test
    void removeCoinFromInventory() {
        vendingMachine.getCoinInventory().refillCoinInventory();
        final int dollarCoinCount = vendingMachine.getCoinInventory().getCoinTypeToQuantity().get(Coin.DOLLAR);

        vendingMachine.getCoinInventory().removeCoinFromInventory(Coin.DOLLAR);
        assertEquals(vendingMachine.getCoinInventory().getCoinTypeToQuantity().get(Coin.DOLLAR),
                dollarCoinCount - 1);
    }

    @Test
    void refillCoinInventory() {
        vendingMachine.setCoinInventory(new CoinInventory(new HashMap<>()));
        assertEquals(0, vendingMachine.getCoinInventory().getCoinTypeToQuantity().size());

        vendingMachine.getCoinInventory().refillCoinInventory();
        assertTrue(vendingMachine.getCoinInventory().getCoinTypeToQuantity().size() > 0);
    }

}
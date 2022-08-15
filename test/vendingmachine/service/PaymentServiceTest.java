package vendingmachine.service;

import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.coin.CoinInventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentServiceTest {

    PaymentService paymentService = new PaymentService();

    @Test
    void getChange() {
        paymentService.refillCoinInventory();
        List<Coin> change = paymentService.getChange(BigDecimal.valueOf(2));

        assertEquals(change.get(0), Coin.TWO_DOLLARS);
    }

    @Test
    void calculateBalance() {
        Map<Coin, Integer> coinTypeToQuantity = new HashMap<>();
        coinTypeToQuantity.put(Coin.DOLLAR, 100);

        VendingMachine.coinInventory = new CoinInventory(coinTypeToQuantity);

        paymentService.calculateBalance();

        assertEquals(BigDecimal.valueOf(100).doubleValue(), VendingMachine.balance.doubleValue());
    }

    @Test
    void refillCoinInventory() {
        Map<Coin, Integer> coinTypeToQuantity = new HashMap<>();

        VendingMachine.coinInventory = new CoinInventory(new HashMap<>());

        assertEquals(0, VendingMachine.coinInventory.coinTypeToQuantity.size());

        paymentService.refillCoinInventory();

        assertTrue(VendingMachine.coinInventory.coinTypeToQuantity.size() > 0);
    }
}
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

    private final VendingMachine vendingMachine = new VendingMachine();
    private final PaymentService paymentService = new PaymentService(vendingMachine);

    @Test
    void getChange() {
        vendingMachine.getCoinInventory().refillCoinInventory();
        List<Coin> change = paymentService.getChange(BigDecimal.valueOf(2));

        assertEquals(change.get(0), Coin.TWO_DOLLARS);
    }

    @Test
    void calculateBalance() {
        vendingMachine.getCoinInventory().getCoinTypeToQuantity().put(Coin.DOLLAR, 100);

        paymentService.calculateBalance();

        assertEquals(BigDecimal.valueOf(100).doubleValue(), vendingMachine.getBalance().doubleValue());
    }
}
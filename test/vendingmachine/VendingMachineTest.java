package vendingmachine;

import org.junit.jupiter.api.Test;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    PaymentService paymentService = new PaymentService();

    @Test
    void setCurrentItem() {
        Item item = new Item("Italian mocha", BigDecimal.valueOf(2.20));
        VendingMachine.setCurrentItem(item);

        assertEquals(item, VendingMachine.currentItem);
    }

    @Test
    void addCurrencyInWaitingStateShouldSucceed() {
        VendingMachine.setState(States.WAITING);

        VendingMachine.setBalance(BigDecimal.valueOf(0));
        VendingMachine.addCurrency(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), VendingMachine.clientMoney);
    }

    @Test
    void addCurrencyInSelectStateShouldNotSucceed() {
        VendingMachine.setState(States.SELECT);

        assertThrows(InvalidOperationException.class,
                () -> VendingMachine.addCurrency(BigDecimal.valueOf(100)));

    }

    @Test
    void selectItemInSelectStateShouldSucceed() {
        VendingMachine.setState(States.SELECT);

        Item item = new Item("Italian latte", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();

        VendingMachine.balance = BigDecimal.valueOf(100);
        VendingMachine.clientMoney = BigDecimal.valueOf(2.00);

        VendingMachine.itemInventory = new ItemInventory(itemToQuantity);
        paymentService.refillCoinInventory();

        VendingMachine.selectItem(item);

        assertEquals(VendingMachine.clientMoney.doubleValue(), BigDecimal.valueOf(0).doubleValue());
        assertEquals(item, VendingMachine.currentItem);
    }

    @Test
    void selectItemInWaitingStateShouldNotSucceed() {
        VendingMachine.setState(States.WAITING);

        Item item = new Item("Italian latte", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();

        VendingMachine.balance = BigDecimal.valueOf(100);
        VendingMachine.clientMoney = BigDecimal.valueOf(2.00);

        VendingMachine.itemInventory = new ItemInventory(itemToQuantity);
        paymentService.refillCoinInventory();

        assertThrows(InvalidOperationException.class,
                () -> VendingMachine.selectItem(item));
    }

    @Test
    void makeItemInPrepareStateShouldSucceed() {
        VendingMachine.setState(States.PREPARING);

        VendingMachine.makeItem();

        assertEquals(VendingMachine.getState(), States.READY);
    }

    @Test
    void makeItemInReadyStateShouldNotSucceed() {
        VendingMachine.setState(States.READY);

        assertThrows(InvalidOperationException.class,
                VendingMachine::makeItem);
    }

    @Test
    void takeItemInReadyStateShouldSucceed() {
        VendingMachine.setState(States.READY);

        VendingMachine.takeItem();

        assertEquals(VendingMachine.getState(), States.WAITING);
    }

    @Test
    void takeItemInWaitingStateShouldNotSucceed() {
        VendingMachine.setState(States.WAITING);

        assertThrows(InvalidOperationException.class,
                VendingMachine::takeItem);
    }

    @Test
    void returnMoneyInWaitingStateShouldSucceed() {
        VendingMachine.setState(States.WAITING);

        VendingMachine.balance = BigDecimal.valueOf(100);
        VendingMachine.clientMoney = BigDecimal.valueOf(2.00);

        paymentService.refillCoinInventory();

        VendingMachine.returnMoney();

        assertEquals(BigDecimal.valueOf(0).doubleValue(), VendingMachine.clientMoney.doubleValue());
    }

    @Test
    void returnMoneyInPrepareStateShouldNotSucceed() {
        VendingMachine.setState(States.PREPARING);

        VendingMachine.balance = BigDecimal.valueOf(100);
        VendingMachine.clientMoney = BigDecimal.valueOf(2.00);

        paymentService.refillCoinInventory();

        VendingMachine.returnMoney();

        assertNotEquals(BigDecimal.valueOf(0).doubleValue(), VendingMachine.clientMoney.doubleValue());
    }

    @Test
    void serviceInWaitingStateShouldSucceed() {
        VendingMachine.setState(States.WAITING);

        VendingMachine.coinInventory = new CoinInventory(new HashMap<>());
        VendingMachine.itemInventory = new ItemInventory(new HashMap<>());

        assertEquals(VendingMachine.itemInventory.itemToQuantity.size(), 0);
        assertEquals(VendingMachine.coinInventory.coinTypeToQuantity.size(), 0);

        VendingMachine.service();

        assertTrue(VendingMachine.itemInventory.itemToQuantity.size() > 0);
        assertTrue(VendingMachine.coinInventory.coinTypeToQuantity.size() > 0);

        assertEquals(States.SELECT, VendingMachine.getState());
    }

    @Test
    void serviceInSelectStateShouldNotSucceed() {
        VendingMachine.setState(States.SELECT);

        VendingMachine.coinInventory = new CoinInventory(new HashMap<>());
        VendingMachine.itemInventory = new ItemInventory(new HashMap<>());

        assertEquals(VendingMachine.itemInventory.itemToQuantity.size(), 0);
        assertEquals(VendingMachine.coinInventory.coinTypeToQuantity.size(), 0);

        assertThrows(InvalidOperationException.class,
                VendingMachine::service);

        assertEquals(VendingMachine.itemInventory.itemToQuantity.size(), 0);
        assertEquals(VendingMachine.coinInventory.coinTypeToQuantity.size(), 0);

        assertEquals(States.WAITING, VendingMachine.getState());
    }

    @Test
    void returnToInitialState() {
        VendingMachine.setState(States.SELECT);

        VendingMachine.returnToInitialState();

        assertEquals(States.WAITING, VendingMachine.getState());
    }

}
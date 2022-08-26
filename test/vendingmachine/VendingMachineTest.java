package vendingmachine;

import org.junit.jupiter.api.Test;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    private final VendingMachine vendingMachine = new VendingMachine();

    @Test
    void setCurrentItem() {
        Item item = new Item("Italian mocha", BigDecimal.valueOf(2.20));
        vendingMachine.setCurrentItem(item);

        assertEquals(item, vendingMachine.getCurrentItem());
    }

    @Test
    void addCurrencyInWaitingStateShouldSucceed() {
        vendingMachine.setState(States.WAITING);


        vendingMachine.setBalance(BigDecimal.valueOf(0));
        vendingMachine.addCurrency(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), vendingMachine.getClientMoney());
    }

    @Test
    void addCurrencyInSelectStateShouldNotSucceed() {
        vendingMachine.setState(States.SELECT);

        assertThrows(InvalidOperationException.class,
                () -> vendingMachine.addCurrency(BigDecimal.valueOf(100)));

    }

    @Test
    void selectItemInSelectStateShouldSucceed() {
        vendingMachine.setState(States.SELECT);

        Item item = new Item("Italian latte", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();

        vendingMachine.setBalance(BigDecimal.valueOf(100));
        vendingMachine.setClientMoney(BigDecimal.valueOf(2.00));

        vendingMachine.setItemInventory(new ItemInventory(itemToQuantity));
        vendingMachine.getCoinInventory().refillCoinInventory();

        vendingMachine.selectItem(item);

        assertEquals(vendingMachine.getClientMoney().doubleValue(), BigDecimal.valueOf(0).doubleValue());
        assertEquals(item, vendingMachine.getCurrentItem());
    }

    @Test
    void selectItemInWaitingStateShouldNotSucceed() {
        vendingMachine.setState(States.WAITING);

        Item item = new Item("Italian latte", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();

        vendingMachine.setBalance(BigDecimal.valueOf(100));
        vendingMachine.setClientMoney(BigDecimal.valueOf(2.00));

        vendingMachine.setItemInventory(new ItemInventory(itemToQuantity));
        vendingMachine.getCoinInventory().refillCoinInventory();

        assertThrows(InvalidOperationException.class,
                () -> vendingMachine.selectItem(item));
    }

    @Test
    void makeItemInPrepareStateShouldSucceed() {
        vendingMachine.setState(States.PREPARING);

        vendingMachine.makeItem();

        assertEquals(vendingMachine.getState(), States.READY);
    }

    @Test
    void makeItemInReadyStateShouldNotSucceed() {
        vendingMachine.setState(States.READY);

        assertThrows(InvalidOperationException.class,
                vendingMachine::makeItem);
    }

    @Test
    void takeItemInReadyStateShouldSucceed() {
        vendingMachine.setState(States.READY);

        vendingMachine.takeItem();

        assertEquals(vendingMachine.getState(), States.WAITING);
    }

    @Test
    void takeItemInWaitingStateShouldNotSucceed() {
        vendingMachine.setState(States.WAITING);

        assertThrows(InvalidOperationException.class,
                vendingMachine::takeItem);
    }

    @Test
    void returnMoneyInWaitingStateShouldSucceed() {
        vendingMachine.setState(States.WAITING);

        vendingMachine.setBalance(BigDecimal.valueOf(100));
        vendingMachine.setClientMoney(BigDecimal.valueOf(2.00));
        vendingMachine.getCoinInventory().refillCoinInventory();

        vendingMachine.returnMoney();

        assertEquals(BigDecimal.valueOf(0).doubleValue(), vendingMachine.getClientMoney().doubleValue());
    }

    @Test
    void returnMoneyInPrepareStateShouldNotSucceed() {
        vendingMachine.setState(States.PREPARING);

        vendingMachine.setBalance(BigDecimal.valueOf(100));
        vendingMachine.setClientMoney(BigDecimal.valueOf(2.00));

        vendingMachine.getCoinInventory().refillCoinInventory();

        vendingMachine.returnMoney();

        assertNotEquals(BigDecimal.valueOf(0).doubleValue(), vendingMachine.getClientMoney().doubleValue());
    }

    @Test
    void serviceInWaitingStateShouldSucceed() {
        vendingMachine.setState(States.WAITING);

        vendingMachine.setCoinInventory(new CoinInventory(new HashMap<>()));
        vendingMachine.setItemInventory(new ItemInventory(new HashMap<>()));

        assertEquals(vendingMachine.getItemInventory().getItemToQuantity().size(), 0);
        assertEquals(vendingMachine.getCoinInventory().getCoinTypeToQuantity().size(), 0);

        vendingMachine.service();

        assertTrue(vendingMachine.getItemInventory().getItemToQuantity().size() > 0);
        assertTrue(vendingMachine.getCoinInventory().getCoinTypeToQuantity().size() > 0);

        assertEquals(States.WAITING, vendingMachine.getState());
    }

    @Test
    void serviceInSelectStateShouldNotSucceed() {
        vendingMachine.setState(States.SELECT);

        vendingMachine.setCoinInventory(new CoinInventory(new HashMap<>()));
        vendingMachine.setItemInventory(new ItemInventory(new HashMap<>()));

        assertEquals(vendingMachine.getItemInventory().getItemToQuantity().size(), 0);
        assertEquals(vendingMachine.getCoinInventory().getCoinTypeToQuantity().size(), 0);

        assertThrows(InvalidOperationException.class,
                vendingMachine::service);

        assertEquals(vendingMachine.getItemInventory().getItemToQuantity().size(), 0);
        assertEquals(vendingMachine.getCoinInventory().getCoinTypeToQuantity().size(), 0);

        assertEquals(States.WAITING, vendingMachine.getState());
    }

    @Test
    void returnToInitialState() {
        vendingMachine.setState(States.SELECT);

        vendingMachine.returnToInitialState();

        assertEquals(States.WAITING, vendingMachine.getState());
    }

}
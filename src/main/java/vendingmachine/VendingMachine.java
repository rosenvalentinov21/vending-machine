package vendingmachine;

import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

import java.math.BigDecimal;
import java.util.HashMap;

public class VendingMachine {
    private static States state = States.WAITING;
    public static BigDecimal balance = BigDecimal.valueOf(0);
    public static BigDecimal clientMoney = BigDecimal.valueOf(0);

    public static Item currentItem = null;

    public static ItemInventory itemInventory = new ItemInventory(new HashMap<>());
    public static CoinInventory coinInventory = new CoinInventory(new HashMap<>());


    public static void setCurrentItem(Item currentItem) {
        VendingMachine.currentItem = currentItem;
    }

    public static void addCurrency(BigDecimal amount) {
        state.addCurrency(amount);
    }

    public static void selectItem(Item item) {
        state.selectItem(item);
    }

    public static void makeItem() {
        state.makeItem();
    }

    public static void takeItem() {
        state.takeItem();
    }

    public static void returnMoney() {
        state.returnMoney();
    }

    public static void service() {
        state.service();
    }

    public static void setState(States state) {
        VendingMachine.state = state;
    }

    public static void returnToInitialState() {
        state = States.WAITING;
    }

    public static void setBalance(BigDecimal balance) {
        VendingMachine.balance = balance;
    }

    public static States getState() {
        return state;
    }
}

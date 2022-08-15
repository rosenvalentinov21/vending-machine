package vendingmachine.service;

import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;

import java.math.BigDecimal;
import java.util.HashMap;

public class ItemService {

    public boolean isInStock(Item item) {
        Item foundItem = VendingMachine.itemInventory.itemToQuantity.keySet().stream()
                .filter(i -> i.name.equals(item.name)).findFirst()
                .orElseThrow(() -> new ItemNotInStockException("The chosen item is not in stock," +
                        "please call service!"));

        return VendingMachine.itemInventory.itemToQuantity.get(foundItem) > 0;
    }

    public void takeItemFromMachine() {
        VendingMachine.setCurrentItem(null);
    }

    public void setSelectedItem(Item item) {
        VendingMachine.setCurrentItem(item);
    }

    public void refillItemInventory() {
        VendingMachine.itemInventory = new ItemInventory(new HashMap<>());
        VendingMachine.itemInventory.itemToQuantity.put(new Item("SHORT_COFFEE", BigDecimal.valueOf(0.60)), 250);
        VendingMachine.itemInventory.itemToQuantity.put(new Item("LONG_COFFEE", BigDecimal.valueOf(0.90)), 200);
        VendingMachine.itemInventory.itemToQuantity.put(new Item("DOUBLE_LONG_COFFEE", BigDecimal.valueOf(1.40)), 180);
        VendingMachine.itemInventory.itemToQuantity.put(new Item("CAPPUCCINO", BigDecimal.valueOf(1.20)), 160);
    }
}

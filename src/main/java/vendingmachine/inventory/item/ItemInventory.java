package vendingmachine.inventory.item;

import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;

import java.math.BigDecimal;
import java.util.Map;

public class ItemInventory {

    private final Map<Item, Integer> itemToQuantity;

    public ItemInventory(final Map<Item, Integer> itemToQuantity) {
        this.itemToQuantity = itemToQuantity;
    }

    public boolean isInStock(final Item item) {
        final Item foundItem = itemToQuantity.keySet().stream()
                .filter(i -> i.name.equals(item.name)).findFirst()
                .orElseThrow(() -> new ItemNotInStockException("The chosen item is not in stock," +
                        "please call service!"));

        return itemToQuantity.get(foundItem) > 0;
    }

    public void takeItemFromMachine(final VendingMachine vendingMachine) {
        vendingMachine.setCurrentItem(null);
    }

    public void setSelectedItem(final Item item,final VendingMachine vendingMachine) {
        vendingMachine.setCurrentItem(item);
    }

    public void refillItemInventory() {
        itemToQuantity.clear();
        itemToQuantity.put(new Item("SHORT_COFFEE", BigDecimal.valueOf(0.60)), 250);
        itemToQuantity.put(new Item("LONG_COFFEE", BigDecimal.valueOf(0.90)), 200);
        itemToQuantity.put(new Item("DOUBLE_LONG_COFFEE", BigDecimal.valueOf(1.40)), 180);
        itemToQuantity.put(new Item("CAPPUCCINO", BigDecimal.valueOf(1.20)), 160);
    }

    public Map<Item, Integer> getItemToQuantity() {
        return itemToQuantity;
    }
}

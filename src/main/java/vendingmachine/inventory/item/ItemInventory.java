package vendingmachine.inventory.item;

import java.util.Map;

public class ItemInventory {

    public Map<Item, Integer> itemToQuantity;


    public ItemInventory(Map<Item, Integer> itemToQuantity) {
        this.itemToQuantity = itemToQuantity;
    }
}

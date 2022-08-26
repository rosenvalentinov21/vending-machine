package vendingmachine.inventory.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemInventoryTest {

    private final VendingMachine vendingMachine = new VendingMachine();

    @Test
    void isInStockShouldSucceed() {
        Item item = new Item("Italian espresso", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        itemToQuantity.put(item, 120);
        vendingMachine.setItemInventory(new ItemInventory(itemToQuantity));

        final boolean inStock = vendingMachine.getItemInventory().isInStock(item);
        Assertions.assertTrue(inStock);
    }

    @Test
    void isInStockShouldThrowException() {
        Item item = new Item("Italian espresso", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        vendingMachine.setItemInventory(new ItemInventory(itemToQuantity));

        assertThrows(ItemNotInStockException.class,
                () -> vendingMachine.getItemInventory().isInStock(item));
    }

    @Test
    void refillItemInventory() {
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        vendingMachine.setItemInventory(new ItemInventory(itemToQuantity));

        Assertions.assertEquals(0, vendingMachine.getItemInventory().getItemToQuantity().size());

        vendingMachine.getItemInventory().refillItemInventory();

        Assertions.assertTrue(vendingMachine.getItemInventory().getItemToQuantity().size() > 0);
    }

}
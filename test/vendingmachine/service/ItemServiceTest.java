package vendingmachine.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;

class ItemServiceTest {

    ItemService itemService = new ItemService();

    @Test
    void isInStockShouldSucceed() {
        Item item = new Item("Italian espresso", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        itemToQuantity.put(item, 120);
        VendingMachine.itemInventory = new ItemInventory(itemToQuantity);

        final boolean inStock = itemService.isInStock(item);
        Assertions.assertTrue(inStock);
    }

    @Test
    void isInStockShouldThrowException() {
        Item item = new Item("Italian espresso", BigDecimal.valueOf(2.00));
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        VendingMachine.itemInventory = new ItemInventory(itemToQuantity);

        assertThrows(ItemNotInStockException.class,
                () -> itemService.isInStock(item));
    }

    @Test
    void refillItemInventory() {
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        VendingMachine.itemInventory = new ItemInventory(itemToQuantity);

        Assertions.assertEquals(0, VendingMachine.itemInventory.itemToQuantity.size());

        itemService.refillItemInventory();

        Assertions.assertTrue(VendingMachine.itemInventory.itemToQuantity.size() > 0);
    }
}
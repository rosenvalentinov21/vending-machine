package vendingmachine.inventory.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.state.States;

class ItemInventoryTest {

  private final ItemInventory itemInventory = new ItemInventory(new HashMap<>());
  private final CoinInventory coinInventory = new CoinInventory(new HashMap<>());
  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
      BigDecimal.ZERO, itemInventory, coinInventory);
  private Map<Item, Integer> itemToQuantity;

  @BeforeEach
  void reinitializeHashTable() {
    itemToQuantity = new HashMap<>();
  }

  @Test
  void isInStockShouldSucceed() {
    final Item item = Item.MOCHA;
    final ItemInventory itemInventory = new ItemInventory(itemToQuantity);
    final int ITEM_QUANTITY = 120;
    itemToQuantity.put(item, ITEM_QUANTITY);

    final boolean inStock = itemInventory.isInStock(item);
    Assertions.assertTrue(inStock);
  }

  @Test
  void isInStockShouldThrowException() {
    final Item item = Item.DOUBLE_LONG_ESPRESSO;
    final ItemInventory itemInventory = new ItemInventory(itemToQuantity);

    assertThrows(ItemNotInStockException.class,
        () -> itemInventory.isInStock(item));
  }

  @Test
  void takeItemFromMachine() {
    final Item item = Item.LATTE;
    final ItemInventory itemInventory = new ItemInventory(itemToQuantity);

    itemInventory.setSelectedItem(item, vendingMachine);
    assertEquals(vendingMachine.getCurrentItem(), item);

    itemInventory.takeItemFromMachine(vendingMachine);
    assertNull(vendingMachine.getCurrentItem());
  }

  @Test
  void setSelectedItem() {
    final Item item = Item.LATTE;
    final ItemInventory itemInventory = new ItemInventory(itemToQuantity);

    itemInventory.setSelectedItem(null, vendingMachine);
    assertNull(vendingMachine.getCurrentItem());

    itemInventory.setSelectedItem(item, vendingMachine);
    assertEquals(vendingMachine.getCurrentItem(), item);
  }

  @Test
  void refillItemInventory() {
    final int EXPECTED_ITEM_TYPES_QUANTITY = 0;
    Assertions.assertEquals(EXPECTED_ITEM_TYPES_QUANTITY,
        itemInventory.getAllItemTypes().size());
    itemInventory.refillItemInventory();
    Assertions.assertTrue(
        itemInventory.getAllItemTypes().size() > EXPECTED_ITEM_TYPES_QUANTITY);
  }

  @Test
  void getAllItems() {
    final Item item = Item.CAPPUCCINO;
    final ItemInventory itemInventory = new ItemInventory(itemToQuantity);
    final int EXPECTED_ITEM_QUANTITY = 1;
    itemToQuantity.put(item, EXPECTED_ITEM_QUANTITY);

    var items = itemInventory.getAllItemTypes();
    assertEquals(items.size(), EXPECTED_ITEM_QUANTITY);
  }
}
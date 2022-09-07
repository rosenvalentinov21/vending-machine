package vendingmachine.inventory.item;

import java.util.List;
import java.util.Map;
import vendingmachine.VendingMachine;
import vendingmachine.exception.ItemNotInStockException;

public class ItemInventory {

  private final Map<Item, Integer> itemToQuantity;

  public ItemInventory(final Map<Item, Integer> itemToQuantity) {
    this.itemToQuantity = itemToQuantity;
  }

  public boolean isInStock(final Item item) {
    try {
      return itemToQuantity.get(item) > 0;
    } catch (Exception e) {
      throw new ItemNotInStockException("The chosen item is not in stock," +
          "please call service!");
    }
  }

  public void takeItemFromMachine(final VendingMachine vendingMachine) {
    vendingMachine.setCurrentItem(null);
  }

  public void setSelectedItem(final Item item, final VendingMachine vendingMachine) {
    vendingMachine.setCurrentItem(item);
  }

  public void refillItemInventory() {
    itemToQuantity.put(Item.ESPRESSO, 250);
    itemToQuantity.put(Item.MOCHA, 200);
    itemToQuantity.put(Item.LATTE, 180);
    itemToQuantity.put(Item.CAPPUCCINO, 160);
    itemToQuantity.put(Item.DOUBLE_LONG_ESPRESSO, 100);
  }

  public List<Item> getAllItemTypes() {
    return itemToQuantity.keySet().stream().toList();
  }

}

package vendingmachine.view.dialog;

import java.math.BigDecimal;
import vendingmachine.inventory.item.Item;

public interface Dialog {

  BigDecimal addCurrency();

  Item selectItem();
}

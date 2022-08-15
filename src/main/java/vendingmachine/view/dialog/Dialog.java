package vendingmachine.view.dialog;

import vendingmachine.inventory.item.Item;

import java.math.BigDecimal;

public interface Dialog {

    BigDecimal addCurrency();
    Item selectItem();
}

package vendingmachine.inventory.item;

import java.math.BigDecimal;

public enum Item {

  ESPRESSO(BigDecimal.valueOf(0.60), 1),
  MOCHA(BigDecimal.valueOf(0.90), 2),
  LATTE(BigDecimal.valueOf(1.20), 3),
  CAPPUCCINO(BigDecimal.valueOf(1.20), 4),
  DOUBLE_LONG_ESPRESSO(BigDecimal.valueOf(2.00), 5);

  public final BigDecimal price;
  public final int order;

  Item(final BigDecimal price, final int order) {
    this.price = price;
    this.order = order;
  }

}

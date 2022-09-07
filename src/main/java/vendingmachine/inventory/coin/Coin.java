package vendingmachine.inventory.coin;

import java.math.BigDecimal;

public enum Coin {
  FIVE_CENTS(BigDecimal.valueOf(0.05), 1),
  TEN_CENTS(BigDecimal.valueOf(0.10), 2),
  QUARTER(BigDecimal.valueOf(0.25), 3),
  FIFTY_CENTS(BigDecimal.valueOf(0.50), 4),
  DOLLAR(BigDecimal.valueOf(1.00), 5),
  TWO_DOLLARS(BigDecimal.valueOf(2.00), 6);

  public final BigDecimal value;
  public final int order;

  Coin(final BigDecimal value, final int order) {
    this.value = value;
    this.order = order;
  }
}

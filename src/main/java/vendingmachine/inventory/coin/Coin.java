package vendingmachine.inventory.coin;

import java.math.BigDecimal;

public enum Coin {

    FIVE_CENTS(BigDecimal.valueOf(0.05)), TEN_CENTS(BigDecimal.valueOf(0.10)), QUARTER(BigDecimal.valueOf(0.25)),
    FIFTY_CENTS(BigDecimal.valueOf(0.50)), DOLLAR(BigDecimal.valueOf(1.00)), TWO_DOLLARS(BigDecimal.valueOf(2.00));

    public final BigDecimal value;

    Coin(BigDecimal value) {
        this.value = value;
    }
}

package vendingmachine.inventory.item;

import java.math.BigDecimal;

public class Item {

    public final String name;

    public final BigDecimal price;

    public Item(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

}

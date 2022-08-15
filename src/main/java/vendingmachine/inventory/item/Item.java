package vendingmachine.inventory.item;

import java.math.BigDecimal;

public class Item {

    public final String name;

    public BigDecimal price;

    public Item(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

}

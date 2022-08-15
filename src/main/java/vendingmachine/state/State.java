package vendingmachine.state;

import vendingmachine.inventory.item.Item;

import java.math.BigDecimal;

public interface State {

    public void addCurrency(BigDecimal amount);

    public void selectItem(Item item);

    public void makeItem();

    public void takeItem();

    public void returnMoney();

    public void service();

    public void endService();

    public States nextState();
}

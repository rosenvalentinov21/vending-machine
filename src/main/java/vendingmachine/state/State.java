package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.inventory.item.Item;

import java.math.BigDecimal;

public interface State {

  void addCurrency(BigDecimal amount, VendingMachine vendingMachine);

  void selectItem(Item item, VendingMachine vendingMachine);

  void makeItem(VendingMachine vendingMachine);

  void takeItem(VendingMachine vendingMachine);

  void returnMoney(VendingMachine vendingMachine);

  void service(VendingMachine vendingMachine);

  void endService(VendingMachine vendingMachine);

  States nextState();
}

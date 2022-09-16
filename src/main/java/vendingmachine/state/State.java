package vendingmachine.state;

import java.math.BigDecimal;
import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.service.PaymentService;

public interface State {

  String EXCEPTION_MESSAGE = "You cannot perform this operation in the current state";
  MessageDisplayer messageDisplayer = new MessageDisplayer();

  default void addCurrency(BigDecimal amount, VendingMachine vendingMachine,
      PaymentService paymentService) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void selectItem(Item item, VendingMachine vendingMachine) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void makeItem(VendingMachine vendingMachine) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void takeItem(VendingMachine vendingMachine) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void returnMoney(VendingMachine vendingMachine, PaymentService paymentService) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void service(VendingMachine vendingMachine) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  default void endService(VendingMachine vendingMachine) {
    throw new InvalidOperationException(EXCEPTION_MESSAGE);
  }

  States nextState();
}

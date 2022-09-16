package vendingmachine.state;

import java.math.BigDecimal;
import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.item.Item;
import vendingmachine.service.PaymentService;

public enum States implements State {

  WAITING {
    @Override
    public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      paymentService.addCurrencyToMachine(amount, vendingMachine);
      vendingMachine.setState(nextState());
    }

    @Override
    public void selectItem(final Item item, final VendingMachine vendingMachine) {
      if (vendingMachine.checkClientBalance(item)) {
        vendingMachine.setState(SELECT);
        vendingMachine.selectItem(item);
      } else {
        messageDisplayer.displayMessage("Please put enough money in the machine.");
      }
    }

    @Override
    public void returnMoney(final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      try {
        paymentService.returnClientMoney(vendingMachine);
      } catch (final NotEnoughCoinsException e) {
        messageDisplayer.displayMessage(e.getMessage());
      }
    }

    @Override
    public void service(final VendingMachine vendingMachine) {
      vendingMachine.setState(SERVICE);
      vendingMachine.service();
    }

    @Override
    public States nextState() {
      return States.SELECT;
    }
  },

  SELECT {
    @Override
    public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      paymentService.addCurrencyToMachine(amount, vendingMachine);
    }

    @Override
    public void selectItem(final Item item, final VendingMachine vendingMachine) {
      try {
        commitPayment(item, vendingMachine);
      } catch (final NotEnoughCoinsException e) {
        messageDisplayer.displayMessage(e.getMessage());
        messageDisplayer.displayMessage("The machine does not have enough money to return you!");
        vendingMachine.setState(States.WAITING);
      }
    }

    private void commitPayment(final Item item, final VendingMachine vendingMachine) {
      if (vendingMachine.checkClientBalance(item)) {
        processPaymentOperations(item, vendingMachine);
        vendingMachine.getItemInventory().setSelectedItem(item, vendingMachine);
        vendingMachine.setState(nextState());
      } else {
        messageDisplayer.displayMessage("Client does not have enough money to take this item!");
        vendingMachine.setState(States.WAITING);
      }
    }

    private void processPaymentOperations(final Item item, final VendingMachine vendingMachine) {
      vendingMachine.subtractFromClientBalance(item.price);
      vendingMachine.returnChange();
    }

    @Override
    public void returnMoney(final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      try {
        paymentService.returnClientMoney(vendingMachine);
      } catch (final NotEnoughCoinsException e) {
        messageDisplayer.displayMessage(e.getMessage());
      }
    }

    @Override
    public void service(final VendingMachine vendingMachine) {
      vendingMachine.setState(States.WAITING);
      throw new InvalidOperationException(
          "Service is available only in waiting state, please try again!");
    }

    @Override
    public States nextState() {
      return States.PREPARING;
    }
  },
  PREPARING {
    @Override
    public void makeItem(final VendingMachine vendingMachine) {
      messageDisplayer.displayMessage("Your item is being prepared please wait....");
      try {
        final long TIME_FOR_PREPARE = 2000L;
        Thread.sleep(TIME_FOR_PREPARE);
      } catch (final InterruptedException e) {
        messageDisplayer.displayMessage("There was a problem with preparing your beverage!");
      }
      vendingMachine.setState(nextState());
    }

    @Override
    public void returnMoney(final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      messageDisplayer.displayMessage("Your money was returned.");
    }

    @Override
    public States nextState() {
      return States.READY;
    }
  },
  READY {
    @Override
    public void makeItem(final VendingMachine vendingMachine) {
      throw new InvalidOperationException("Your item is ready, please take it and try again.");
    }

    @Override
    public void takeItem(final VendingMachine vendingMachine) {
      messageDisplayer.displayMessage("Your item is ready, thanks for your purchase!");
      vendingMachine.getItemInventory().takeItemFromMachine(vendingMachine);
      vendingMachine.setState(nextState());
    }

    @Override
    public void returnMoney(final VendingMachine vendingMachine,
        final PaymentService paymentService) {
      messageDisplayer.displayMessage("Your money was returned.");
    }

    @Override
    public States nextState() {
      return States.WAITING;
    }
  },
  SERVICE {
    @Override
    public void service(final VendingMachine vendingMachine) {
      refillInventories(vendingMachine);
      calculateTotalMachineBalance(vendingMachine);
      endService(vendingMachine);
    }

    private void refillInventories(final VendingMachine vendingMachine) {
      vendingMachine.getItemInventory().refillItemInventory();
      vendingMachine.getCoinInventory().refillCoinInventory();
    }

    private void calculateTotalMachineBalance(final VendingMachine vendingMachine) {
      vendingMachine.calculateCoinInventoryBalance();
    }

    @Override
    public void endService(final VendingMachine vendingMachine) {
      messageDisplayer.displayMessage("Item and coin inventories were refilled successfully!");
      vendingMachine.setState(nextState());
    }

    @Override
    public States nextState() {
      return States.WAITING;
    }
  }

}

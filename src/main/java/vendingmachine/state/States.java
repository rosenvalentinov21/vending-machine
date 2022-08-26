package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

public enum States implements State {

    WAITING {
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine) {
            final PaymentService paymentService = new PaymentService(vendingMachine);
            paymentService.updateBalance(amount);
            paymentService.addToClientBalance(amount);
            vendingMachine.setState(nextState());
        }

        @Override
        public void selectItem(final Item item, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You cannot select item before you have enough money!");
        }

        @Override
        public void makeItem(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You have not purchased any item yet!");
        }

        @Override
        public void takeItem(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("The container is empty, please purchase your item first!");
        }

        @Override
        public void returnMoney(final VendingMachine vendingMachine) {
            try {
                returnClientMoney(vendingMachine);
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
            }
        }

        @Override
        public void service(final VendingMachine vendingMachine) {
            refillInventories(vendingMachine);
            calculateTotalMachineBalance(vendingMachine);
            endService(vendingMachine);
        }

        private void calculateTotalMachineBalance(final VendingMachine vendingMachine) {
            final PaymentService paymentService = new PaymentService(vendingMachine);
            paymentService.calculateBalance();
        }

        private void refillInventories(final VendingMachine vendingMachine) {
            vendingMachine.getItemInventory().refillItemInventory();
            vendingMachine.getCoinInventory().refillCoinInventory();
        }

        @Override
        public void endService(final VendingMachine vendingMachine) {
            messageDisplayer.displayMessage("Item and coin inventories were refilled successfully!");
        }

        @Override
        public States nextState() {
            return States.SELECT;
        }
    },

    SELECT {
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You cannot add currency in this state, your money was returned!");
        }

        @Override
        public void selectItem(final Item item, final VendingMachine vendingMachine) {
            try {
                commitPayment(item, vendingMachine);
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
                messageDisplayer.displayMessage("The machine does not have enough money to return you!");
                vendingMachine.setState(States.WAITING);
            }
        }

        private void commitPayment(final Item item, final VendingMachine vendingMachine) {
            final PaymentService paymentService = new PaymentService(vendingMachine);
            if (paymentService.checkIfClientCanAfford(item.price)) {
                processPaymentOperations(item, vendingMachine, paymentService);
                vendingMachine.getItemInventory().setSelectedItem(item, vendingMachine);
                vendingMachine.setState(nextState());
            } else {
                messageDisplayer.displayMessage("Client does not have enough money to take this item!");
                vendingMachine.setState(States.WAITING);
            }
        }

        private void processPaymentOperations(final Item item, final VendingMachine vendingMachine, final PaymentService paymentService) {
            final List<Coin> change = paymentService
                    .getChange(vendingMachine.getClientMoney().subtract(item.price));
            takeMoneyFromClientBalance(item, paymentService);
            returnChangeToClient(item, vendingMachine, paymentService, change);
        }

        private void returnChangeToClient(final Item item, final VendingMachine vendingMachine, final PaymentService paymentService, final List<Coin> change) {
            if (vendingMachine.getClientMoney().compareTo(item.price) > 0) {
                paymentService.returnChange(change);
                paymentService.takeFromClientBalance(vendingMachine.getClientMoney());
            }
        }

        private void takeMoneyFromClientBalance(final Item item, final PaymentService paymentService) {
            paymentService.takeFromClientBalance(item.price);
            paymentService.updateBalance(item.price);
        }

        @Override
        public void makeItem(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You have to select your item first!");
        }

        @Override
        public void takeItem(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("The container is empty, please select your item first!");
        }

        @Override
        public void returnMoney(final VendingMachine vendingMachine) {
            try {
                returnClientMoney(vendingMachine);
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
            }
        }

        @Override
        public void service(final VendingMachine vendingMachine) {
            vendingMachine.setState(States.WAITING);
            throw new InvalidOperationException("Service is available only in waiting state, please try again!");
        }

        @Override
        public void endService(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public States nextState() {
            return States.PREPARING;
        }
    },
    PREPARING {
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You cannot add currency in this state!");
        }

        @Override
        public void selectItem(final Item item, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You have selected an item already!");
        }

        @Override
        public void makeItem(final VendingMachine vendingMachine) {
            messageDisplayer.displayMessage("Your item is being prepared please wait....");
            try {
                final long TIME_FOR_PREPARE = 2000L;
                Thread.sleep(TIME_FOR_PREPARE);
            } catch (InterruptedException e) {
                messageDisplayer.displayMessage("There was a problem with preparing your beverage!");
            }

            vendingMachine.setState(nextState());
        }

        @Override
        public void takeItem(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Your item is still being prepared, please wait!");
        }

        @Override
        public void returnMoney(final VendingMachine vendingMachine) {
            messageDisplayer.displayMessage("Your money was returned.");
        }

        @Override
        public void service(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public void endService(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public States nextState() {
            return States.READY;
        }
    },
    READY {
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(final BigDecimal amount, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("You cannot add currency in this state!");
        }

        @Override
        public void selectItem(final Item item, final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Your item is ready, please take it and try again.");
        }

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
        public void returnMoney(final VendingMachine vendingMachine) {
            messageDisplayer.displayMessage("Your money was returned.");
        }

        @Override
        public void service(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public void endService(final VendingMachine vendingMachine) {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public States nextState() {
            return States.WAITING;
        }
    };

    private static void returnClientMoney(final VendingMachine vendingMachine) {
        final PaymentService paymentService = new PaymentService(vendingMachine);
        BigDecimal clientMoney = vendingMachine.getClientMoney();
        paymentService.returnChange(paymentService.getChange(clientMoney));
        paymentService.takeFromClientBalance(clientMoney);
    }
}

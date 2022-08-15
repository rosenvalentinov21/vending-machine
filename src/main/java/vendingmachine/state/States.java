package vendingmachine.state;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.service.ItemService;
import vendingmachine.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

public enum States implements State {

    WAITING {
        private final ItemService itemService = new ItemService();
        private final PaymentService paymentService = new PaymentService();
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(BigDecimal amount) {
            paymentService.updateBalance(amount);
            paymentService.addToClientBalance(amount);
            VendingMachine.setState(nextState());
        }

        @Override
        public void selectItem(Item item) {
            throw new InvalidOperationException("You cannot select item before you have enough money!");
        }

        @Override
        public void makeItem() {
            throw new InvalidOperationException("You have not purchased any item yet!");
        }

        @Override
        public void takeItem() {
            throw new InvalidOperationException("The container is empty, please purchase your item first!");
        }

        @Override
        public void returnMoney() {
            try {
                BigDecimal clientMoney = VendingMachine.clientMoney;
                paymentService.returnChange(paymentService.getChange(clientMoney));
                paymentService.takeFromClientBalance(clientMoney);
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
            }
        }

        @Override
        public void service() {
            itemService.refillItemInventory();
            paymentService.refillCoinInventory();
            paymentService.calculateBalance();
            endService();
        }

        @Override
        public void endService() {
            messageDisplayer.displayMessage("Item and coin inventories were refilled successfully!");
            VendingMachine.setState(nextState());
        }

        @Override
        public States nextState() {
            return States.SELECT;
        }
    },

    SELECT {
        private final PaymentService paymentService = new PaymentService();
        private final ItemService itemService = new ItemService();
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(BigDecimal amount) {
            try {
                paymentService.returnChange(paymentService.getChange(amount));
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
            }
            VendingMachine.setState(States.WAITING);
            throw new InvalidOperationException("You cannot add currency in this state, your money was returned!");
        }

        @Override
        public void selectItem(Item item) {
            try {
                if (paymentService.checkClientBalance(item)) {
                    List<Coin> change = paymentService
                            .getChange(VendingMachine.clientMoney.subtract(item.price));
                    paymentService.takeFromClientBalance(item.price);
                    paymentService.updateBalance(item.price);
                    itemService.setSelectedItem(item);
                    VendingMachine.setState(nextState());
                    if (VendingMachine.clientMoney.compareTo(item.price) != 0) {
                        paymentService.returnChange(change);
                    }

                } else {
                    messageDisplayer.displayMessage("Client does not have enough money to take this item!");
                    VendingMachine.setState(States.WAITING);
                }
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
                messageDisplayer.displayMessage("The machine does not have enough money to return you!");
                VendingMachine.setState(States.WAITING);
            }
        }

        @Override
        public void makeItem() {
            throw new InvalidOperationException("You have to select your item first!");
        }

        @Override
        public void takeItem() {
            throw new InvalidOperationException("The container is empty, please select your item first!");
        }

        @Override
        public void returnMoney() {
            try {
                BigDecimal clientMoney = VendingMachine.clientMoney;
                paymentService.returnChange(paymentService.getChange(clientMoney));
                paymentService.takeFromClientBalance(clientMoney);
            } catch (NotEnoughCoinsException e) {
                messageDisplayer.displayMessage(e.getMessage());
            }
        }

        @Override
        public void service() {
            VendingMachine.setState(States.WAITING);
            throw new InvalidOperationException("Service is available only in waiting state, please try again!");
        }

        @Override
        public void endService() {
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
        public void addCurrency(BigDecimal amount) {
            throw new InvalidOperationException("You cannot add currency in this state!");
        }

        @Override
        public void selectItem(Item item) {
            throw new InvalidOperationException("You have selected an item already!");
        }

        @Override
        public void makeItem() {
            messageDisplayer.displayMessage("Your item is being prepared please wait....");
            try {
                long TIME_FOR_PREPARE = 2000L;
                Thread.sleep(TIME_FOR_PREPARE);
            } catch (InterruptedException e) {
                messageDisplayer.displayMessage("There was a problem with preparing your beverage!");
            }

            VendingMachine.setState(nextState());
        }

        @Override
        public void takeItem() {
            throw new InvalidOperationException("Your item is still being prepared, please wait!");
        }

        @Override
        public void returnMoney() {
            messageDisplayer.displayMessage("Your money was returned.");
        }

        @Override
        public void service() {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public void endService() {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public States nextState() {
            return States.READY;
        }
    },
    READY {
        private final ItemService itemService = new ItemService();
        private final MessageDisplayer messageDisplayer = new MessageDisplayer();

        @Override
        public void addCurrency(BigDecimal amount) {
            throw new InvalidOperationException("You cannot add currency in this state!");
        }

        @Override
        public void selectItem(Item item) {
            throw new InvalidOperationException("Your item is ready, please take it and try again.");
        }

        @Override
        public void makeItem() {
            throw new InvalidOperationException("Your item is ready, please take it and try again.");
        }

        @Override
        public void takeItem() {
            messageDisplayer.displayMessage("Your item is ready, thanks for your purchase!");
            itemService.takeItemFromMachine();
            VendingMachine.setState(nextState());
        }

        @Override
        public void returnMoney() {
            messageDisplayer.displayMessage("Your money was returned.");
        }

        @Override
        public void service() {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public void endService() {
            throw new InvalidOperationException("Service is available only in waiting state!");
        }

        @Override
        public States nextState() {
            return States.WAITING;
        }
    }
}

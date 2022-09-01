package vendingmachine.service;

import vendingmachine.VendingMachine;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.messaging.MessageDisplayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vendingmachine.inventory.coin.Coin.*;

public class PaymentService {

    private final VendingMachine vendingMachine;

    public PaymentService(final VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    private final MessageDisplayer messageDisplayer = new MessageDisplayer();

    public void returnChange(final List<Coin> change) {

        if (!change.isEmpty()) {
            change.forEach(coin -> messageDisplayer.displayMessage("Pop , you get returned " +
                    coin.value));
        } else {
            messageDisplayer.displayMessage("You have not put any money yet, or " +
                    "the machine does not have enough money to return you!");
        }
    }

    public List<Coin> getChange(BigDecimal clientMoney) {
        final List<Coin> change = new ArrayList<>();

        while (clientMoney.compareTo(BigDecimal.valueOf(0)) != 0) {
            clientMoney = getCoinIfInStock(clientMoney, change);
        }
        return change;
    }

    //TODO use final for clientMoney parameter. Refactor that method. 
    //As an idea, you could tell the vending machine to return the change and it could decide how.
    //Or you could create a separate class for that logic.
    private BigDecimal getCoinIfInStock(BigDecimal clientMoney, final List<Coin> change) {
        if (clientMoney.compareTo(Coin.TWO_DOLLARS.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(Coin.TWO_DOLLARS)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(TWO_DOLLARS, change));
        } else if (clientMoney.compareTo(Coin.DOLLAR.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(Coin.DOLLAR)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(DOLLAR, change));
        } else if (clientMoney.compareTo(FIFTY_CENTS.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(FIFTY_CENTS)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(FIFTY_CENTS, change));
        } else if (clientMoney.compareTo(QUARTER.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(QUARTER)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(QUARTER, change));
        } else if (clientMoney.compareTo(TEN_CENTS.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(TEN_CENTS)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(TEN_CENTS, change));
        } else if (clientMoney.compareTo(Coin.FIVE_CENTS.value) >= 0 &&
                vendingMachine.getCoinInventory().hasCoin(Coin.FIVE_CENTS)) {
            clientMoney = clientMoney.subtract(addChangeToListIfAvailable(FIVE_CENTS, change));
        } else {
            throw new NotEnoughCoinsException("There is not enough coins to return to client," +
                    "you may want to service the machine!");
        }
        return clientMoney;
    }

    private BigDecimal addChangeToListIfAvailable(final Coin coin,final List<Coin> change) {
        vendingMachine.getCoinInventory().removeCoinFromInventory(coin);
        change.add(coin);
        return coin.value;
    }

    public boolean checkIfClientCanAfford(final BigDecimal itemPrice) {
        return vendingMachine.getClientMoney().compareTo(itemPrice) >= 0;
    }

    public void updateBalance(final BigDecimal amount) {
        vendingMachine.setBalance(amount);
    }

    public void addToClientBalance(final BigDecimal amount) {
        vendingMachine.setClientMoney(vendingMachine.getClientMoney().add(amount));
    }

    public void takeFromClientBalance(final BigDecimal amount) {
        vendingMachine.setClientMoney(vendingMachine.getClientMoney().subtract(amount));
    }

    public void calculateBalance() {
        final var coinInventory = vendingMachine.getCoinInventory().getCoinTypeToQuantity();

        final List<Coin> coinTypes = coinInventory.keySet().stream().toList();

        vendingMachine.setBalance(aggregateCoinValues(coinInventory, coinTypes));
    }

    private BigDecimal aggregateCoinValues(final Map<Coin, Integer> coinInventory,final List<Coin> coinTypes) {
    	//TODO Use BigDecimal.Zero instead.
        BigDecimal balance = new BigDecimal(0);
        for (final Coin coin : coinTypes) {
            final Integer count = coinInventory.get(coin);
            balance = balance.add(coin.value.multiply(BigDecimal.valueOf(count)));
        }
        return balance;
    }
}
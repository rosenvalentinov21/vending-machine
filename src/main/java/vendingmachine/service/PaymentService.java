package vendingmachine.service;

import vendingmachine.VendingMachine;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static vendingmachine.inventory.coin.Coin.*;

public class PaymentService {
    private final MessageDisplayer messageDisplayer = new MessageDisplayer();

    public void returnChange(List<Coin> change) {

        if (!change.isEmpty()) {
            change.forEach(coin -> messageDisplayer.displayMessage("Pop , you get returned " +
                    coin.value));
        } else {
            messageDisplayer.displayMessage("You have not put any money yet, or " +
                    "the machine does not have enough money to return you!");
        }
    }

    public List<Coin> getChange(BigDecimal clientMoney) {
        List<Coin> change = new ArrayList<>();

        while (clientMoney.compareTo(BigDecimal.valueOf(0)) != 0) {
            if (clientMoney.compareTo(Coin.TWO_DOLLARS.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(Coin.TWO_DOLLARS)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(TWO_DOLLARS, change));
            } else if (clientMoney.compareTo(Coin.DOLLAR.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(Coin.DOLLAR)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(DOLLAR, change));
            } else if (clientMoney.compareTo(FIFTY_CENTS.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(FIFTY_CENTS)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(FIFTY_CENTS, change));
            } else if (clientMoney.compareTo(QUARTER.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(QUARTER)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(QUARTER, change));
            } else if (clientMoney.compareTo(TEN_CENTS.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(TEN_CENTS)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(TEN_CENTS, change));
            } else if (clientMoney.compareTo(Coin.FIVE_CENTS.value) >= 0 &&
                    VendingMachine.coinInventory.hasCoin(Coin.FIVE_CENTS)) {
                clientMoney = clientMoney.subtract(addChangeToListIfAvailable(FIVE_CENTS, change));
            } else {
                throw new NotEnoughCoinsException("There is not enough coins to return to client," +
                        "you may want to service the machine!");
            }
        }
        return change;
    }

    private BigDecimal addChangeToListIfAvailable(Coin coin, List<Coin> change) {
        VendingMachine.coinInventory.getCoin(coin);
        change.add(coin);
        return coin.value;
    }

    public boolean checkClientBalance(Item item) {
        return VendingMachine.clientMoney.compareTo(item.price) >= 0;
    }

    public void updateBalance(BigDecimal amount) {
        VendingMachine.balance = VendingMachine.balance.add(amount);
    }

    public void addToClientBalance(BigDecimal amount) {
        VendingMachine.clientMoney = VendingMachine.clientMoney.add(amount);
    }

    public void takeFromClientBalance(BigDecimal amount) {
        VendingMachine.clientMoney = VendingMachine.clientMoney.subtract(amount);
    }

    public void calculateBalance() {
        var coinInventory = VendingMachine.coinInventory.getCoinTypeToQuantity();

        List<Coin> coinTypes = coinInventory.keySet().stream().toList();

        BigDecimal balance = BigDecimal.valueOf(0);

        for (Coin coin : coinTypes) {
            Integer count = coinInventory.get(coin);
            balance = balance.add(coin.value.multiply(BigDecimal.valueOf(count)));
            VendingMachine.setBalance(balance);
        }
    }

    public void refillCoinInventory() {
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.FIVE_CENTS, 50);
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.TEN_CENTS, 40);
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.QUARTER, 35);
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.FIFTY_CENTS, 30);
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.DOLLAR, 25);
        VendingMachine.coinInventory.coinTypeToQuantity.put(Coin.TWO_DOLLARS, 20);
    }
}
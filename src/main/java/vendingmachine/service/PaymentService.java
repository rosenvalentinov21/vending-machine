package vendingmachine.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import vendingmachine.VendingMachine;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.messaging.MessageDisplayer;

public class PaymentService {

  private final VendingMachine vendingMachine;

  public PaymentService(final VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  private final MessageDisplayer messageDisplayer = new MessageDisplayer();

  public void returnChangeToClient(final List<Coin> change) {

    if (!change.isEmpty()) {
      change.forEach(coin -> messageDisplayer.displayMessage("Pop , you get returned " +
          coin.value));
    } else {
      messageDisplayer.displayMessage("You have not put any money yet, or " +
          "the machine does not have enough money to return you!");
    }
  }

  public List<Coin> getChange(final BigDecimal clientMoney) {
    final List<Coin> change = new ArrayList<>();

    Coin returnedCoin;
    BigDecimal moneyLeft = clientMoney;

    while (moneyLeft.compareTo(BigDecimal.ZERO) > 0) {
      returnedCoin = addChangeToList(change, moneyLeft);
      moneyLeft = extractChangeFromBalances(returnedCoin, moneyLeft);
    }
    return change;
  }

  private Coin addChangeToList(final List<Coin> change, final BigDecimal moneyLeft) {
    Coin returnedCoin = returnCoinToClient(moneyLeft);
    change.add(returnedCoin);
    return returnedCoin;
  }

  private BigDecimal extractChangeFromBalances(final Coin returnedCoin,
      final BigDecimal moneyLeft) {
    BigDecimal money = moneyLeft.subtract(returnedCoin.value);
    vendingMachine.getCoinInventory().removeCoinFromInventory(returnedCoin);
    return money;
  }

  private Coin returnCoinToClient(final BigDecimal clientMoney) {

    final List<Coin> valuesDescendingByCoinValue = getCoinsOrderedByValuesDescending();

    for (final Coin coin : valuesDescendingByCoinValue) {
      if (getCoin(clientMoney, coin) != null) {
        return coin;
      }
    }

    throw new NotEnoughCoinsException("There is not enough coins to return to client," +
        "you may want to service the machine!");
  }

  private List<Coin> getCoinsOrderedByValuesDescending() {
    return Arrays.stream(Coin.values())
        .sorted(Comparator.comparing(coin -> coin.value))
        .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
          Collections.reverse(list);
          return list;
        }));
  }

  private Coin getCoin(final BigDecimal clientMoney, final Coin coin) {
    if (clientMoney.compareTo(coin.value) >= 0 &&
        vendingMachine.getCoinInventory().hasCoin(coin)) {
      return coin;
    }
    return null;
  }

  public boolean checkIfClientCanAfford(final BigDecimal itemPrice) {
    return vendingMachine.getClientMoney().compareTo(itemPrice) >= 0;
  }

  public void updateBalance(final BigDecimal amount) {
    vendingMachine.setBalance(vendingMachine.getBalance().add(amount));
  }

  public void addToClientBalance(final BigDecimal amount) {
    vendingMachine.setClientMoney(vendingMachine.getClientMoney().add(amount));
  }

  public void takeFromClientBalance(final BigDecimal amount) {
    vendingMachine.setClientMoney(vendingMachine.getClientMoney().subtract(amount));
  }

  public void calculateMachineBalance() {
    vendingMachine.setBalance(vendingMachine.getCoinInventory().calculateBalance());
  }
}
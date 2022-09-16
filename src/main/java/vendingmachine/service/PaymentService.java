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
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.messaging.MessageDisplayer;

public class PaymentService {

  private final MessageDisplayer messageDisplayer = new MessageDisplayer();

  public void addCurrencyToMachine(final BigDecimal amount,
      final VendingMachine vendingMachine) {
    vendingMachine.updateMachineBalance(amount);
    vendingMachine.addToClientBalance(amount);
  }

  public void returnClientMoney(final VendingMachine vendingMachine) {
    final List<Coin> change = getChange(vendingMachine.getClientMoney(),
        vendingMachine.getCoinInventory());
    vendingMachine.subtractFromClientBalance(vendingMachine.getClientMoney());
    vendingMachine.calculateCoinInventoryBalance();
    displayChangeReturnedToClient(change);
  }

  public boolean clientCanAffordItem(final BigDecimal itemPrice, final BigDecimal clientMoney) {
    return clientMoney.compareTo(itemPrice) >= 0;
  }

  public BigDecimal balanceWithNewAmount(final BigDecimal amount, final BigDecimal balance) {
    return balance.add(amount);
  }

  public BigDecimal clientBalanceWithNewAmount(final BigDecimal amount,
      final BigDecimal clientMoney) {
    return clientMoney.add(amount);
  }

  public BigDecimal clientBalanceWithSubtractedAmount(final BigDecimal amount,
      final BigDecimal clientMoney) {
    return clientMoney.subtract(amount);
  }

  public BigDecimal calculateMachineBalance(final CoinInventory coinInventory) {
    return coinInventory.calculateBalance();
  }

  private void displayChangeReturnedToClient(final List<Coin> change) {
    if (!change.isEmpty()) {
      change.forEach(coin -> messageDisplayer.displayMessage("Pop , you get returned " +
          coin.value));
    } else {
      messageDisplayer.displayMessage("You have not put any money yet, or " +
          "the machine does not have enough money to return you!");
    }
  }

  private List<Coin> getChange(final BigDecimal clientMoney, final CoinInventory coinInventory) {
    final List<Coin> change = new ArrayList<>();

    Coin returnedCoin;
    BigDecimal moneyLeft = clientMoney;

    while (moneyLeft.compareTo(BigDecimal.ZERO) > 0) {
      returnedCoin = addChangeToList(change, moneyLeft, coinInventory);
      moneyLeft = extractChangeFromBalances(returnedCoin, moneyLeft, coinInventory);
    }
    return change;
  }

  private Coin addChangeToList(final List<Coin> change, final BigDecimal moneyLeft,
      final CoinInventory coinInventory) {
    Coin returnedCoin = getCoinFromInventory(moneyLeft, coinInventory);
    change.add(returnedCoin);
    return returnedCoin;
  }

  private BigDecimal extractChangeFromBalances(final Coin returnedCoin,
      final BigDecimal moneyLeft, final CoinInventory coinInventory) {
    BigDecimal money = moneyLeft.subtract(returnedCoin.value);
    coinInventory.removeCoinFromInventory(returnedCoin);
    return money;
  }

  private Coin getCoinFromInventory(final BigDecimal clientMoney,
      final CoinInventory coinInventory) {

    final List<Coin> valuesDescendingByCoinValue = getCoinsOrderedByValuesDescending();

    for (final Coin coin : valuesDescendingByCoinValue) {
      if (getCoin(clientMoney, coin, coinInventory) != null) {
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

  private Coin getCoin(final BigDecimal clientMoney, final Coin coin,
      final CoinInventory coinInventory) {
    if (clientMoney.compareTo(coin.value) >= 0 &&
        coinInventory.hasCoin(coin)) {
      return coin;
    }
    return null;
  }
}
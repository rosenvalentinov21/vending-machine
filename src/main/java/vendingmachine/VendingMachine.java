package vendingmachine;

import java.math.BigDecimal;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

public class VendingMachine {

  private States state;
  private BigDecimal balance;
  private BigDecimal clientMoney;

  private final ItemInventory itemInventory;
  private final CoinInventory coinInventory;

  public VendingMachine(final States state, final BigDecimal balance,
      final BigDecimal clientMoney, final ItemInventory itemInventory,
      final CoinInventory coinInventory) {
    this.state = state;
    this.balance = balance;
    this.clientMoney = clientMoney;
    this.itemInventory = itemInventory;
    this.coinInventory = coinInventory;
  }

  private Item currentItem = null;

  public void setCurrentItem(final Item currentItem) {
    this.currentItem = currentItem;
  }

  public Item getCurrentItem() {
    return currentItem;
  }

  public void addCurrency(final BigDecimal amount) {
    state.addCurrency(amount, this);
  }

  public void selectItem(final Item item) {
    state.selectItem(item, this);
  }

  public void makeItem() {
    state.makeItem(this);
  }

  public void takeItem() {
    state.takeItem(this);
  }

  public void returnMoney() {
    state.returnMoney(this);
  }

  public void service() {
    state.service(this);
  }

  public void setState(final States state) {
    this.state = state;
  }

  public void returnToInitialState() {
    state = States.WAITING;
  }

  public States getState() {
    return state;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(final BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getClientMoney() {
    return clientMoney;
  }

  public void setClientMoney(final BigDecimal clientMoney) {
    this.clientMoney = clientMoney;
  }

  public ItemInventory getItemInventory() {
    return itemInventory;
  }

  public CoinInventory getCoinInventory() {
    return coinInventory;
  }
}
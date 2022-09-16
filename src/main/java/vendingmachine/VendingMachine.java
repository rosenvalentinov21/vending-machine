package vendingmachine;

import java.math.BigDecimal;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;

public class VendingMachine {

  private States state;
  private BigDecimal balance;
  private BigDecimal clientMoney;

  private Item currentItem = null;

  private final ItemInventory itemInventory;
  private final CoinInventory coinInventory;

  private final PaymentService paymentService;

  public VendingMachine(final States state, final BigDecimal balance,
      final BigDecimal clientMoney, final ItemInventory itemInventory,
      final CoinInventory coinInventory, final PaymentService paymentService) {
    this.state = state;
    this.balance = balance;
    this.clientMoney = clientMoney;
    this.itemInventory = itemInventory;
    this.coinInventory = coinInventory;
    this.paymentService = paymentService;
  }

  public void addCurrency(final BigDecimal amount) {
    state.addCurrency(amount, this, paymentService);
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

  public void returnChange() {
    state.returnMoney(this, paymentService);
  }

  public void service() {
    state.service(this);
  }

  public boolean checkClientBalance(final Item item) {
    return paymentService.clientCanAffordItem(item.price, clientMoney);
  }

  public void addToClientBalance(final BigDecimal amount) {
    clientMoney = paymentService.clientBalanceWithNewAmount(amount, clientMoney);
  }

  public void subtractFromClientBalance(final BigDecimal amount) {
    clientMoney = paymentService.clientBalanceWithSubtractedAmount(amount, clientMoney);
  }

  public void updateMachineBalance(final BigDecimal amount) {
    balance = paymentService.balanceWithNewAmount(amount, balance);
  }

  public void calculateCoinInventoryBalance() {
    balance = paymentService.calculateMachineBalance(coinInventory);
  }

  public void setCurrentItem(final Item currentItem) {
    this.currentItem = currentItem;
  }

  public Item getCurrentItem() {
    return currentItem;
  }

  public States getState() {
    return state;
  }

  public void setState(final States state) {
    this.state = state;
  }

  public void returnToInitialState() {
    state = States.WAITING;
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
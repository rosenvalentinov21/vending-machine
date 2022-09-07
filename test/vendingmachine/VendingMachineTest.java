package vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.service.PaymentService;
import vendingmachine.state.States;

class VendingMachineTest {

  private final ItemInventory itemInventory = new ItemInventory(new HashMap<>());
  private final CoinInventory coinInventory = new CoinInventory(new HashMap<>());
  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
      BigDecimal.ZERO, itemInventory, coinInventory);
  private final PaymentService paymentService = new PaymentService(vendingMachine);
  private final BigDecimal CURRENCY = BigDecimal.valueOf(100);
  private final BigDecimal CLIENT_MONEY = BigDecimal.ONE;


  @Test
  void setCurrentItem() {
    final Item item = Item.ESPRESSO;
    vendingMachine.setCurrentItem(item);

    assertEquals(item, vendingMachine.getCurrentItem());
  }

  @Test
  void addCurrencyInWaitingStateShouldSucceed() {
    vendingMachine.setState(States.WAITING);
    vendingMachine.addCurrency(CURRENCY);
    assertEquals(CURRENCY, vendingMachine.getClientMoney());
  }

  @Test
  void selectItemInSelectStateShouldSucceed() {
    vendingMachine.setState(States.WAITING);
    vendingMachine.service();

    vendingMachine.setState(States.SELECT);

    final Item item = Item.DOUBLE_LONG_ESPRESSO;

    vendingMachine.setBalance(Item.DOUBLE_LONG_ESPRESSO.price);
    vendingMachine.setClientMoney(Item.DOUBLE_LONG_ESPRESSO.price);

    vendingMachine.selectItem(item);

    assertEquals(vendingMachine.getClientMoney().doubleValue(),
        BigDecimal.ZERO.doubleValue());
    assertEquals(item, vendingMachine.getCurrentItem());
  }

  @Test
  void selectItemInWaitingStateShouldNotSucceed() {
    vendingMachine.setState(States.WAITING);

    final Item item = Item.CAPPUCCINO;

    vendingMachine.service();

    vendingMachine.setBalance(Item.CAPPUCCINO.price);
    vendingMachine.setClientMoney(Item.CAPPUCCINO.price);

    assertThrows(InvalidOperationException.class,
        () -> vendingMachine.selectItem(item));
  }

  @Test
  void makeItemInPrepareStateShouldSucceed() {
    vendingMachine.setState(States.PREPARING);

    vendingMachine.makeItem();

    assertEquals(vendingMachine.getState(), States.READY);
  }

  @Test
  void makeItemInReadyStateShouldNotSucceed() {
    vendingMachine.setState(States.READY);

    assertThrows(InvalidOperationException.class,
        vendingMachine::makeItem);
  }

  @Test
  void takeItemInReadyStateShouldSucceed() {
    vendingMachine.setState(States.READY);

    vendingMachine.takeItem();

    assertEquals(vendingMachine.getState(), States.WAITING);
  }

  @Test
  void takeItemInWaitingStateShouldNotSucceed() {
    vendingMachine.setState(States.WAITING);

    assertThrows(InvalidOperationException.class,
        vendingMachine::takeItem);
  }

  @Test
  void returnMoneyInWaitingStateShouldSucceed() {
    vendingMachine.setState(States.WAITING);

    vendingMachine.setBalance(CURRENCY);
    vendingMachine.setClientMoney(CLIENT_MONEY);
    vendingMachine.getCoinInventory().refillCoinInventory();

    vendingMachine.returnMoney();

    assertEquals(BigDecimal.ZERO.doubleValue(),
        vendingMachine.getClientMoney().doubleValue());
  }

  @Test
  void returnMoneyInPrepareStateShouldNotSucceed() {
    vendingMachine.setState(States.PREPARING);

    vendingMachine.setBalance(CURRENCY);
    vendingMachine.setClientMoney(CLIENT_MONEY);

    vendingMachine.getCoinInventory().refillCoinInventory();

    vendingMachine.returnMoney();

    assertNotEquals(BigDecimal.ZERO.doubleValue(),
        vendingMachine.getClientMoney().doubleValue());
  }

  @Test
  void serviceInWaitingStateShouldSucceed() {
    vendingMachine.setState(States.WAITING);

    final int EXPECTED_INVENTORY_SIZE = 0;
    assertEquals(vendingMachine.getItemInventory().getAllItemTypes().size(),
        EXPECTED_INVENTORY_SIZE);

    vendingMachine.service();
    paymentService.calculateMachineBalance();

    assertTrue(
        vendingMachine.getItemInventory().getAllItemTypes().size() > EXPECTED_INVENTORY_SIZE);
    assertTrue(vendingMachine.getBalance().doubleValue() > BigDecimal.ZERO.doubleValue());

    assertEquals(States.WAITING, vendingMachine.getState());
  }

  @Test
  void serviceInSelectStateShouldNotSucceed() {
    vendingMachine.setState(States.SELECT);

    assertEquals(vendingMachine.getItemInventory().getAllItemTypes().size(), 0);

    assertThrows(InvalidOperationException.class,
        vendingMachine::service);

    assertEquals(vendingMachine.getItemInventory().getAllItemTypes().size(), 0);

    assertEquals(States.WAITING, vendingMachine.getState());
  }

  @Test
  void returnToInitialState() {
    vendingMachine.setState(States.SELECT);

    vendingMachine.returnToInitialState();

    assertEquals(States.WAITING, vendingMachine.getState());
  }

}
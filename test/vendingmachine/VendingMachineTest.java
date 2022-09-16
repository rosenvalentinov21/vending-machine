package vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
  private final PaymentService paymentService = mock(PaymentService.class);
  private final VendingMachine vendingMachine = new VendingMachine(States.WAITING, BigDecimal.ZERO,
      BigDecimal.ZERO, itemInventory, coinInventory, paymentService);
  private final BigDecimal CURRENCY = BigDecimal.valueOf(100);


  @Test
  void setCurrentItem() {
    final Item item = Item.ESPRESSO;
    vendingMachine.setCurrentItem(item);

    assertEquals(item, vendingMachine.getCurrentItem());
  }

  @Test
  void addCurrencyInWaitingState_ShouldAddCurrency() {
    vendingMachine.setState(States.WAITING);
    vendingMachine.addCurrency(CURRENCY);

    verify(paymentService).addCurrencyToMachine(any(), any());
  }

  @Test
  void selectItemInSelectState_ShouldSelectItem() {
    vendingMachine.setState(States.SELECT);

    final boolean EXPECTED_RESULT = true;
    final Item item = Item.DOUBLE_LONG_ESPRESSO;

    when(paymentService.clientCanAffordItem(any(), any())).thenReturn(EXPECTED_RESULT);

    vendingMachine.selectItem(item);

    assertEquals(item, vendingMachine.getCurrentItem());
  }

  @Test
  void selectItemInSelectState_WhenClientCanNotAfford_ShouldNotSelect() {
    vendingMachine.setState(States.SELECT);

    final boolean EXPECTED_RESULT = false;
    final Item item = Item.DOUBLE_LONG_ESPRESSO;

    when(paymentService.clientCanAffordItem(any(), any())).thenReturn(EXPECTED_RESULT);

    vendingMachine.selectItem(item);

    assertNull(vendingMachine.getCurrentItem());
  }


  @Test
  void selectItemInWaiting_WhenNotEnoughMoney_ShouldNotSelectAnyItem() {
    vendingMachine.setState(States.WAITING);

    final boolean EXPECTED_RESULT = false;
    final Item item = Item.CAPPUCCINO;

    when(paymentService.clientCanAffordItem(any(), any())).thenReturn(EXPECTED_RESULT);

    vendingMachine.selectItem(item);

    assertNull(vendingMachine.getCurrentItem());
  }

  @Test
  void selectItemInWaiting_WhenEnoughMoney_ShouldSelectItem() {
    vendingMachine.setState(States.WAITING);

    final boolean EXPECTED_RESPONSE = true;
    final Item EXPECTED_ITEM = Item.CAPPUCCINO;

    when(paymentService.clientCanAffordItem(any(), any())).thenReturn(EXPECTED_RESPONSE);

    vendingMachine.selectItem(EXPECTED_ITEM);

    assertEquals(vendingMachine.getCurrentItem(), EXPECTED_ITEM);
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
    vendingMachine.returnChange();

    verify(paymentService).returnClientMoney(any());
  }

  @Test
  void returnMoneyInPrepareStateShouldNotSucceed() {
    vendingMachine.setState(States.PREPARING);
    vendingMachine.returnChange();

    verify(paymentService, times(0)).returnClientMoney(any());
  }

  @Test
  void serviceInWaitingState_ShouldSucceed() {
    vendingMachine.setState(States.WAITING);

    vendingMachine.service();

    assertEquals(vendingMachine.getState(), States.WAITING);
    verify(paymentService).calculateMachineBalance(any());
  }

  @Test
  void serviceInSelectState_ShouldNotSucceed() {
    vendingMachine.setState(States.SELECT);

    assertThrows(InvalidOperationException.class, vendingMachine::service);
  }

  @Test
  void returnToInitialState() {
    vendingMachine.setState(States.SELECT);

    vendingMachine.returnToInitialState();

    assertEquals(States.WAITING, vendingMachine.getState());
  }

  @Test
  void addToClientBalance_ShouldIncrementClientBalance() {
    final BigDecimal EXPECTED_BALANCE = BigDecimal.TEN;

    when(paymentService.clientBalanceWithNewAmount(any(), any())).thenReturn(EXPECTED_BALANCE);

    vendingMachine.setClientMoney(BigDecimal.ZERO);
    assertEquals(vendingMachine.getClientMoney(), BigDecimal.ZERO);

    vendingMachine.addToClientBalance(EXPECTED_BALANCE);
    assertEquals(vendingMachine.getClientMoney(), EXPECTED_BALANCE);
  }

  @Test
  void updateMachineBalance_ShouldIncrementMachineBalance() {
    final BigDecimal EXPECTED_BALANCE = BigDecimal.TEN;

    when(paymentService.balanceWithNewAmount(any(), any())).thenReturn(EXPECTED_BALANCE);

    vendingMachine.setBalance(BigDecimal.ZERO);
    assertEquals(vendingMachine.getBalance(), BigDecimal.ZERO);

    vendingMachine.updateMachineBalance(EXPECTED_BALANCE);
    assertEquals(vendingMachine.getBalance(), EXPECTED_BALANCE);
  }

}
package vendingmachine;

import vendingmachine.inventory.coin.CoinInventory;
import vendingmachine.inventory.item.Item;
import vendingmachine.inventory.item.ItemInventory;
import vendingmachine.state.States;

import java.math.BigDecimal;
import java.util.HashMap;

public class VendingMachine {
	// TODO Initialize fields in constructor
	private States state = States.WAITING;
	private BigDecimal balance = BigDecimal.valueOf(0);
	private BigDecimal clientMoney = BigDecimal.valueOf(0);

	private Item currentItem = null;

	private ItemInventory itemInventory = new ItemInventory(new HashMap<>());
	private CoinInventory coinInventory = new CoinInventory(new HashMap<>());

	public void setCurrentItem(Item currentItem) {
		this.currentItem = currentItem;
	}

	public Item getCurrentItem() {
		return currentItem;
	}

	public void addCurrency(BigDecimal amount) {
		state.addCurrency(amount, this);
	}

	public void selectItem(Item item) {
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

	public void setState(States state) {
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

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getClientMoney() {
		return clientMoney;
	}

	public void setClientMoney(BigDecimal clientMoney) {
		this.clientMoney = clientMoney;
	}

	public ItemInventory getItemInventory() {
		return itemInventory;
	}

	public void setItemInventory(ItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}

	public CoinInventory getCoinInventory() {
		return coinInventory;
	}

	public void setCoinInventory(CoinInventory coinInventory) {
		this.coinInventory = coinInventory;
	}
}
package vendingmachine.view.dialog;

import static vendingmachine.inventory.coin.Coin.values;

import java.math.BigDecimal;
import java.util.Arrays;
import vendingmachine.exception.InvalidCoinInputException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.messaging.Reader;

public class VendingMachineDialog implements Dialog {

  private final MessageDisplayer messageDisplayer;
  private final Reader reader;

  public VendingMachineDialog(final MessageDisplayer messageDisplayer,
      final Reader reader) {
    this.messageDisplayer = messageDisplayer;
    this.reader = reader;
  }

  @Override
  public BigDecimal addCurrency() {
    BigDecimal amount = BigDecimal.ZERO;
    String command;

    final String EXIT_COMMAND = String.valueOf(Coin.values().length + 1);

    boolean finished = false;
    while (!finished) {
      command = getChosenCoin();

      if (command.equals(EXIT_COMMAND)) {
        finished = true;
      } else {
        amount = addAmount(amount, command);
      }
    }

    messageDisplayer.displayMessage("Thanks, you inserted " + amount + " dollars.");
    return amount;
  }

  private String getChosenCoin() {
    displayCoins();
    messageDisplayer.displayMessage("Choice: ");
    return reader.readNextLine();
  }


  private BigDecimal addAmount(BigDecimal amount, final String command) {
    try {
      amount = amount.add(matchCommand(command));
    } catch (InvalidCoinInputException ex) {
      messageDisplayer.displayMessage("Please enter a valid coin!");
    }
    return amount;
  }

  private void displayCoins() {
    messageDisplayer.displayMessage("Choose coin type to insert: ");

    var coinsByValuesAscending = Arrays.stream(values()).toList();
    for (int i = 0; i < coinsByValuesAscending.size(); i++) {
      messageDisplayer.displayMessage(values()[i].order + ") " + values()[i].value);
    }
    messageDisplayer.displayMessage((values().length + 1) + ") " + "finish");
  }

  private BigDecimal matchCommand(final String command) {
    for (final Coin coin : values()) {
      if (command.equals(String.valueOf(coin.order))) {
        return coin.value;
      }
    }
    throw new InvalidCoinInputException("Please enter a valid coin");
  }

  @Override
  public Item selectItem() {
    int command;
    while (true) {
      command = chooseItemFromList();

      final Item item = getChosenItem(command);
      if (item != null) {
        return item;
      }
    }
  }

  private Item getChosenItem(int command) {
    for (Item item : Item.values()) {
      if (command == item.order) {
        return item;
      }
    }
    messageDisplayer.displayMessage("Please choose a valid item!");
    return null;
  }


  private int chooseItemFromList() {
    messageDisplayer.displayMessage("Choose an item from our list: ");
    displayItems();
    messageDisplayer.displayMessage("Choose: ");
    return reader.readNextInteger();
  }

  private void displayItems() {
    int counter = 1;
    for (Item item : Item.values()) {
      messageDisplayer.displayMessage(counter + ". " + item + " , price = " + item.price);
      counter++;
    }
  }
}

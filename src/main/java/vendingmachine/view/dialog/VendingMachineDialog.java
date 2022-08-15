package vendingmachine.view.dialog;

import vendingmachine.VendingMachine;
import vendingmachine.exception.InvalidCoinInputException;
import vendingmachine.inventory.coin.Coin;
import vendingmachine.inventory.item.Item;
import vendingmachine.messaging.MessageDisplayer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;

public class VendingMachineDialog implements Dialog {

    private final MessageDisplayer messageDisplayer = new MessageDisplayer();

    @Override
    public BigDecimal addCurrency() {
        final Scanner sc = new Scanner(System.in);
        BigDecimal amount = BigDecimal.valueOf(0);
        String command;

        while (true) {
            messageDisplayer.displayMessage("Choose coin type to insert: ");
            messageDisplayer.displayMessage("1) 5 cents ");
            messageDisplayer.displayMessage("2) 10 cents ");
            messageDisplayer.displayMessage("3) 25 cents ");
            messageDisplayer.displayMessage("4) 50 cents ");
            messageDisplayer.displayMessage("5) 1 dollar ");
            messageDisplayer.displayMessage("6) 2 dollars ");
            messageDisplayer.displayMessage("7) finish ");
            messageDisplayer.displayMessage("Choice: ");

            command = sc.nextLine();

            if (command.equals("7")) {
                break;
            } else {
                try {
                    amount = amount.add(checkCommand(command));
                } catch (InvalidCoinInputException ex) {
                    messageDisplayer.displayMessage("Please enter a valid coin!");
                }
            }
        }

        messageDisplayer.displayMessage("Thanks, you inserted " + amount + " dollars.");
        return amount;
    }

    private BigDecimal checkCommand(String command) {
        BigDecimal value = switch (command) {
            case "1" -> Coin.FIVE_CENTS.value;
            case "2" -> Coin.TEN_CENTS.value;
            case "3" -> Coin.QUARTER.value;
            case "4" -> Coin.FIFTY_CENTS.value;
            case "5" -> Coin.DOLLAR.value;
            case "6" -> Coin.TWO_DOLLARS.value;
            default -> BigDecimal.valueOf(0);
        };

        if (!Objects.equals(value, BigDecimal.valueOf(0))) {
            return value;
        } else throw new InvalidCoinInputException("Please enter a valid coin");
    }

    @Override
    public Item selectItem() {
        final Scanner sc = new Scanner(System.in);
        int command;

        Item item = null;
        boolean selected = false;

        while (!selected) {

            messageDisplayer.displayMessage("Choose an item from our list: ");
            int counter = 1;
            for (Item i : VendingMachine.itemInventory.itemToQuantity.keySet()) {
                messageDisplayer.displayMessage(counter + ". " + i.name + " , price = " + i.price);
                counter++;
            }
            messageDisplayer.displayMessage("Choose: ");
            command = sc.nextInt();
            sc.nextLine();

            int matcher = 1;
            for (Item i : VendingMachine.itemInventory.itemToQuantity.keySet()) {
                if (command == matcher++) {
                    item = new Item(i.name, i.price);
                    selected = true;
                    break;
                }
            }

            if (!selected) {
                messageDisplayer.displayMessage("Please choose a valid item!");
            }
        }
        return item;
    }
}

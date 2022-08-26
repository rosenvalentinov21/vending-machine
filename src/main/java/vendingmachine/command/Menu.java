package vendingmachine.command;

import vendingmachine.exception.InvalidOperationException;
import vendingmachine.messaging.MessageDisplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<Option> options = new ArrayList<>();
    private final MessageDisplayer messageDisplayer = new MessageDisplayer();
    private final Scanner scanner = new Scanner(System.in);

    public void addOption(final Option option) {
        options.add(option);
    }

    public void executeOption(final Option option) {
        option.executeCommand();
    }

    public void showOptions() {
        int counter = 1;
        for (Option option : options) {
            messageDisplayer.displayMessage(counter + ") " + option.getTitle());
            counter++;
        }
    }

    public Option chooseOption() {
        while (true) {
            messageDisplayer.displayMessage("Choose an option");
            int choice = scanner.nextInt();

            try {
                return matchOption(choice);
            } catch (InvalidOperationException e) {
                messageDisplayer.displayMessage("Your choice does not match any option.");
            }
        }
    }

    private Option matchOption(final int choice) {
        int matcher = 1;
        for (Option option : options) {
            if (matcher == choice) {
                return option;
            }
            matcher++;
        }
        throw new InvalidOperationException("No such option.");
    }
}

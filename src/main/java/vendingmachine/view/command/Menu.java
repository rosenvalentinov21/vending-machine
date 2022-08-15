package vendingmachine.view.command;

import vendingmachine.exception.InvalidCoinInputException;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.exception.NotEnoughCoinsException;
import vendingmachine.messaging.MessageDisplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final String title;
    private final List<Option> options;
    private final MessageDisplayer messageDisplayer = new MessageDisplayer();

    public static class Option {
        private final String text;
        private final Command command;

        public Option(String text, Command command) {
            this.text = text;
            this.command = command;
        }

        public String getText() {
            return text;
        }

        public Command getCommand() {
            return command;
        }

        @Override
        public String toString() {
            return "MenuOption{" +
                    "text='" + text + '\'' +
                    ", command=" + command +
                    '}';
        }
    }

    public Menu(final String title, List<Option> options) {
        class ExitCommand implements Command {
            @Override
            public void execute() {
                messageDisplayer.displayMessage(String.format("Exiting menu '%s'.%n", title));
            }
        }
        this.title = title;
        this.options = new ArrayList<>(options);
        // add exit option as last option in menu
        this.options.add(new Option("Exit", new ExitCommand()));
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            messageDisplayer.displayMessage(String.format("\nMENU: %s%n", title));
            for (int i = 0; i < options.size(); i++) {
                messageDisplayer.displayMessage(String.format("%2d. %s%n", i + 1, options.get(i).getText()));
            }

            int choice = -1;
            do {
                messageDisplayer.displayMessage(String.format("Enter your choice (1 - %s):", options.size()));
                var choiceStr = scanner.nextLine();
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException ex) {
                    messageDisplayer.displayMessage("Error: Invalid choice. Please enter a valid number between 1 and " + options.size());
                }
            } while (choice < 1 || choice > options.size());
            try {
                options.get(choice - 1).getCommand().execute();
            } catch (InvalidCoinInputException | InvalidOperationException | NotEnoughCoinsException ex) {
                messageDisplayer.displayMessage("Error: " + ex.getMessage());
                if (ex.getCause() != null && ex.getCause() instanceof Exception) {
                    messageDisplayer.displayMessage((ex.getCause()).getMessage());
                }
            } catch (Exception ex) {
                messageDisplayer.displayMessage("Error: " + ex.getMessage());
            }

            if (choice == options.size()) { // Exit command chosen
                break;
            }
        }
    }

}

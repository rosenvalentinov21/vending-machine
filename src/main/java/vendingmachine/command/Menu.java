package vendingmachine.command;

import java.util.ArrayList;
import java.util.List;
import vendingmachine.exception.InvalidOperationException;
import vendingmachine.messaging.MessageDisplayer;
import vendingmachine.messaging.Reader;

public class Menu {

  private final List<Option> options;
  private final MessageDisplayer messageDisplayer;
  private final Reader reader;

  public Menu(final MessageDisplayer messageDisplayer,
      final Reader reader) {
    this.options = new ArrayList<>();
    this.messageDisplayer = messageDisplayer;
    this.reader = reader;
  }

  public void addOption(final Option option) {
    options.add(option);
  }

  public ProceedResponse executeOption(final Option option) {
    return option.executeCommand();
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
      final int choice = reader.readNextInteger();

      try {
        return matchOption(choice);
      } catch (InvalidOperationException e) {
        messageDisplayer.displayMessage("Your choice does not match any option.");
      }
    }
  }

  private Option matchOption(final int choice) {
    int matcher = 1;
    for (final Option option : options) {
      if (matcher == choice) {
        return option;
      }
      matcher++;
    }
    throw new InvalidOperationException("No such option.");
  }
}

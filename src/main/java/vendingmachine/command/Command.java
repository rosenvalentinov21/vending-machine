package vendingmachine.command;

import vendingmachine.VendingMachine;
import vendingmachine.messaging.MessageDisplayer;

public abstract class Command {

  protected final VendingMachine vendingMachine;
  protected final MessageDisplayer messageDisplayer = new MessageDisplayer();
  protected final ProceedResponse proceedResponse = new ProceedResponse(true);

  public Command(final VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  public abstract ProceedResponse execute();
}
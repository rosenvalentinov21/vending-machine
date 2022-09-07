package vendingmachine.command;

public class ProceedResponse {

  public void setProceed(final boolean proceed) {
    this.proceed = proceed;
  }

  private boolean proceed;

  public ProceedResponse(final boolean proceed) {
    this.proceed = proceed;
  }

  public boolean isProceed() {
    return proceed;
  }
}

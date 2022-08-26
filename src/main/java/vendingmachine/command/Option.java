package vendingmachine.command;

public class Option {
    private final String title;
    private final Command command;

    public Option(final String title,final Command command) {
        this.title = title;
        this.command = command;
    }

    public String getTitle() {
        return title;
    }

    public void executeCommand() {
        command.execute();
    }

}

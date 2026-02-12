package commands;

public class ReverseCommand implements Command {
    @Override
    public CommandResult execute(String str, String original) {
        String reversed = new StringBuilder(str).reverse().toString();
        return new CommandResult(reversed, false);
    }
}

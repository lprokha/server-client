package commands;

public class UpperCommand implements Command {
    @Override
    public CommandResult execute(String str, String original) {
        return new CommandResult(str.toUpperCase(), false);
    }
}

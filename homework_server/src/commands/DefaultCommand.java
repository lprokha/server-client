package commands;

public class DefaultCommand implements Command {
    @Override
    public CommandResult execute(String str, String original) {
        return new CommandResult(original, false);
    }
}

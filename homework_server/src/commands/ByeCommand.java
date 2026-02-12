package commands;

public class ByeCommand implements Command {
    @Override
    public CommandResult execute(String str, String original) {
        return new CommandResult("Bye bye!", true);
    }
}

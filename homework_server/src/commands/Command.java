package commands;

public interface Command {
    CommandResult execute(String str, String original);
}

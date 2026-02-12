package commands;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements Command {

    @Override
    public CommandResult execute(String str, String original) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new CommandResult(time, false);
    }
}

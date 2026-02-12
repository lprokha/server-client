package commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommand implements Command {

    @Override
    public CommandResult execute(String str, String original) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(date, false);
    }
}

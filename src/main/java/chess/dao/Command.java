package chess.dao;

import java.util.function.IntSupplier;
import org.springframework.dao.DataAccessException;

public class Command {

    private final IntSupplier command;

    public Command(IntSupplier command) {
        this.command = command;
    }

    public CommandResult executeOrThrow(String exceptionMessage) {
        try {
            return new CommandResult(command.getAsInt());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public static CommandResult execute(IntSupplier command) {
        return new CommandResult(command.getAsInt());
    }
}

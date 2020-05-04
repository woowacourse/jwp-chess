package chess.command;

import chess.progress.Progress;

public class EndCommand implements Command {
    private static final String COMMAND = "end";

    private final String value;

    public EndCommand(String value) {
        this.value = value;
    }

    public static boolean isEnd(String command) {
        String lowerCaseCommand = command.toLowerCase();
        return COMMAND.equals(lowerCaseCommand);
    }

    @Override
    public Progress conduct() {
        if (COMMAND.equals(value)) {
            return Progress.END;
        }
        return Progress.ERROR;
    }
}

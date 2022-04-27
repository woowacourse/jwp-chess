package chess.mapper;

public enum Command {

    START("start"),
    END("end"),
    MOVE("move"),
    STATUS("status");

    private final String value;

    Command(final String value) {
        this.value = value;
    }

    public static boolean isMove(final String command) {
        return command.startsWith(Command.MOVE.value);
    }
}

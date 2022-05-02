package chess.domain.command;

public class MoveCommand {

    private static final int SOURCE_POSITION = 1;
    private static final int TARGET_POSITION = 2;
    private static final String COMMAND_INPUT_DELIMITER = " ";

    private final String source;
    private final String target;

    public MoveCommand(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static MoveCommand of(String command) {
        String[] commandInput = command.split(COMMAND_INPUT_DELIMITER);
        return new MoveCommand(
                commandInput[SOURCE_POSITION],
                commandInput[TARGET_POSITION]);
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}

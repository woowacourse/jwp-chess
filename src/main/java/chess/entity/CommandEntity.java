package chess.entity;

public class CommandEntity {

    private static final Long EMPTY_ID = 0L;

    private final Long commandId;
    private final Long roomId;
    private final String command;

    public CommandEntity(long commandId, long roomId, String command) {
        this.commandId = commandId;
        this.roomId = roomId;
        this.command = command;
    }

    public CommandEntity(long roomId, String command) {
        this(EMPTY_ID, roomId, command);
    }

    public Long getCommandId() {
        return commandId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getCommand() {
        return command;
    }
}

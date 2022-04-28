package chess.entity;

public class CommandEntity {

    private final Long commandId;
    private final Long roomId;
    private final String command;

    public CommandEntity(long commandId, long roomId, String command) {
        this.commandId = commandId;
        this.roomId = roomId;
        this.command = command;
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

package chess.entity;

public class CommandEntity {

    private final Long roomId;
    private final String command;

    public CommandEntity(long roomId, String command) {
        this.roomId = roomId;
        this.command = command;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getCommand() {
        return command;
    }
}

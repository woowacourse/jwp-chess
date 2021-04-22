package chess.dto;

public class MoveRequestDto {
    private final long id;
    private final String command;

    public MoveRequestDto(long id, String command) {
        this.id = id;
        this.command = command;
    }

    public long getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }
}

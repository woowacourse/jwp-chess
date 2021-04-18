package chess.dto;

public class MoveRequestDto {
    private final String roomName;
    private final String command;

    public MoveRequestDto(String roomName, String command) {
        this.roomName = roomName;
        this.command = command;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCommand() {
        return command;
    }
}

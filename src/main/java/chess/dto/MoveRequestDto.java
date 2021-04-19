package chess.dto;

public class MoveRequestDto {
    private final String command;
    private final int roomNo;

    public MoveRequestDto(String command, int roomNo) {
        this.command = command;
        this.roomNo = roomNo;
    }

    public String getCommand() {
        return command;
    }

    public int getRoomNo() {
        return roomNo;
    }
}

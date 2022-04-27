package chess.web.dto;

public class StartResultDto {
    private int roomId;

    public StartResultDto() {
    }

    public StartResultDto(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}

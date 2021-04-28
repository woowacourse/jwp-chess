package chess.dto;

public class ChessRoomDto {
    private Long roomId;

    public ChessRoomDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

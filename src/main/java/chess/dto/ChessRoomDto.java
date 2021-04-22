package chess.dto;

public class ChessRoomDto {
    Long roomId;

    public ChessRoomDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

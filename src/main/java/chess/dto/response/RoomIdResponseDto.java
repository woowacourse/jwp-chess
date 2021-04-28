package chess.dto.response;

public class RoomIdResponseDto {
    private final Long roomId;

    public RoomIdResponseDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

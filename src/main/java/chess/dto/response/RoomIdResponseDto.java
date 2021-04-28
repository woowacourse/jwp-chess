package chess.dto.response;

public class RoomIdResponseDto {
    private final Long roomId;

    public RoomIdResponseDto(final Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

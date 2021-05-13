package chess.controller.web.dto.room;

public class RoomIdResponseDto {

    private Long roomId;

    public RoomIdResponseDto(final Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

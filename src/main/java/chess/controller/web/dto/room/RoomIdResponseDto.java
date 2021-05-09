package chess.controller.web.dto.room;

public class RoomIdResponseDto {

    private Long roomId;

    public RoomIdResponseDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}

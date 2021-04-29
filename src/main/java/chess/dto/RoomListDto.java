package chess.dto;

public class RoomListDto {
    private final Long roomId;
    private final String roomName;

    public RoomListDto(final Long roomId, final String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}

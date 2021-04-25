package chess.dto.request;

public class RoomRequestDto {
    private final String roomName;

    public RoomRequestDto(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}

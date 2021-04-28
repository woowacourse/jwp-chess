package chess.dto.request;

public class RoomNameRequestDto {
    private String roomName;

    public RoomNameRequestDto(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}

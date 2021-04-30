package chess.dto;

public class RoomsDto {
    private final Long roomId;
    private final String roomName;

    public RoomsDto(final Long roomId, final String roomName) {
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

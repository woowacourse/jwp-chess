package chess.dto;

public class RemoveRoomDto {

    private final long roomId;
    private final String password;

    public RemoveRoomDto(long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}

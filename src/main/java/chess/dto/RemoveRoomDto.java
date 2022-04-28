package chess.dto;

public class RemoveRoomDto {

    private final Long roomId;
    private final String password;

    public RemoveRoomDto(Long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}

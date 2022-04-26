package chess.dto.request;

public class RoomDeletionRequestDto {

    private int roomId;
    private String password;

    public RoomDeletionRequestDto() {
    }

    public RoomDeletionRequestDto(final int roomId, final String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}

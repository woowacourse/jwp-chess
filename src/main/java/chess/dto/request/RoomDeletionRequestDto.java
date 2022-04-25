package chess.dto.request;

public class RoomDeletionRequestDto {

    private int roomId;
    private String password;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(final int roomId) {
        this.roomId = roomId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}

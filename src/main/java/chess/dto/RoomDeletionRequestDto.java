package chess.dto;

public class RoomDeletionRequestDto {

    private int roomId;
    private String password;

    public int getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }

    public void setRoomId(final int roomId) {
        this.roomId = roomId;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}

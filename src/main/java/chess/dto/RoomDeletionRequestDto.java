package chess.dto;

public class RoomDeletionRequestDto {
    private String password;
    private Integer roomId;

    public RoomDeletionRequestDto() {
    }

    public RoomDeletionRequestDto(final String password, final Integer roomId) {
        this.password = password;
        this.roomId = roomId;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "RoomDeletionRequestDto{" +
                "password='" + password + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}

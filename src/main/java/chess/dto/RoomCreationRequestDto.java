package chess.dto;

public class RoomCreationRequestDto {
    private final String roomName;
    private final String password;

    public RoomCreationRequestDto() {
        this(null, null);
    }

    public RoomCreationRequestDto(final String roomName, final String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "RoomCreationRequestDto{" +
                "roomName='" + roomName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

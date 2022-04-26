package chess.dto.request;

public class RoomCreationRequestDto {

    private String roomName;
    private String password;

    public RoomCreationRequestDto() {
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
}

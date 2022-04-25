package chess.dto;

public class RoomCreationRequestDto {

    private String roomName;
    private String password;

    public RoomCreationRequestDto() {
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}

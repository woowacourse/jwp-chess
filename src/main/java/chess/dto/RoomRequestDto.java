package chess.dto;

public class RoomRequestDto {

    private String roomName;
    private String password;
    private String whiteName;
    private String blackName;

    public RoomRequestDto(String roomName, String password, String whiteName, String blackName) {
        this.roomName = roomName;
        this.password = password;
        this.whiteName = whiteName;
        this.blackName = blackName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }
}

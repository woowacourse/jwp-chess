package chess.dto;

public class CreateRoomDto {

    private String roomName;
    private String roomPassword;

    protected CreateRoomDto() {

    }

    public CreateRoomDto(String roomName, String roomPassword) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    @Override
    public String toString() {
        return "CreateRoomDto{" +
            "roomName='" + roomName + '\'' +
            ", roomPassword='" + roomPassword + '\'' +
            '}';
    }
}

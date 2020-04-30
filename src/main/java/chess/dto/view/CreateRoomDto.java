package chess.dto.view;

public class CreateRoomDto {

    private String roomName;
    private String roomPassword;

    protected CreateRoomDto() {
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

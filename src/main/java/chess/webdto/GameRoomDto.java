package chess.webdto;

public class GameRoomDto {
    private final int roomId;
    private final String roomName;

    public GameRoomDto(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}

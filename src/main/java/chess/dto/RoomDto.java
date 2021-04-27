package chess.dto;

public class RoomDto {
    private String roomName;
    private int roomId;

    public RoomDto() {
    }

    private RoomDto(String roomName, int roomId) {
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public static RoomDto of(String roomName, int roomId) {
        return new RoomDto(roomName, roomId);
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomId() {
        return roomId;
    }
}

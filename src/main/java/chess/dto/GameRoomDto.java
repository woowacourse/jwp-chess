package chess.dto;

public class GameRoomDto {

    private int id;
    private String roomName;
    private String whiteName;
    private String blackName;

    public GameRoomDto() {
    }

    public GameRoomDto(int id, String roomName, String whiteName, String blackName) {
        this.id = id;
        this.roomName = roomName;
        this.whiteName = whiteName;
        this.blackName = blackName;
    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }
}

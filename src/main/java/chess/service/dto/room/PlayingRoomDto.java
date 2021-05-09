package chess.service.dto.room;

public class PlayingRoomDto {

    private Long roomId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public PlayingRoomDto(Long roomId, String roomName, String whiteUserName, String blackUserName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }
}

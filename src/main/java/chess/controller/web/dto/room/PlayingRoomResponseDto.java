package chess.controller.web.dto.room;

public class PlayingRoomResponseDto {

    private Long roomId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public PlayingRoomResponseDto() {
    }

    public PlayingRoomResponseDto(Long roomId, String roomName, String whiteUserName, String blackUserName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public void setWhiteUserName(String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }

    public void setBlackUserName(String blackUserName) {
        this.blackUserName = blackUserName;
    }
}

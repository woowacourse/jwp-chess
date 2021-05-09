package chess.controller.web.dto.room;

public class PlayingRoomResponseDto {

    private Long roomId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public PlayingRoomResponseDto() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(final Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public void setWhiteUserName(final String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }

    public void setBlackUserName(final String blackUserName) {
        this.blackUserName = blackUserName;
    }
}

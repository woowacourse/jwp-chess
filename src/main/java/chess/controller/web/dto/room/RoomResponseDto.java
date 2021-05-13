package chess.controller.web.dto.room;

public class RoomResponseDto {

    private Long id;
    private Long gameId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public RoomResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(final Long gameId) {
        this.gameId = gameId;
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

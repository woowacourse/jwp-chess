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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

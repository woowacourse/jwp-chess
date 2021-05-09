package chess.service.dto.room;

public class RoomDto {

    private Long id;
    private Long gameId;
    private String roomName;
    private String whiteUserName;
    private String blackUserName;

    public RoomDto(Long id, Long gameId, String roomName, String whiteUserName, String blackUserName) {
        this.id = id;
        this.gameId = gameId;
        this.roomName = roomName;
        this.whiteUserName = whiteUserName;
        this.blackUserName = blackUserName;
    }

    public Long getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
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

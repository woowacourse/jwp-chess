package chess.dto.request;

public class RoomExitRequest {
    private final Long roomId;
    private final String userName;
    private final Long gameId;

    public RoomExitRequest(final Long roomId, final String userName, final Long gameId) {
        this.roomId = roomId;
        this.userName = userName;
        this.gameId = gameId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getGameId() {
        return gameId;
    }
}

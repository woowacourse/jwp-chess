package chess.mysql;

class RoomDto {
    private final long roomId;
    private final String roomName;
    private final long gameId;
    private final long blackUserId;
    private final long whiteUserId;

    public RoomDto(long roomId, String roomName, long gameId, long blackUserId, long whiteUserId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameId = gameId;
        this.blackUserId = blackUserId;
        this.whiteUserId = whiteUserId;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public long getGameId() {
        return gameId;
    }

    public long getBlackUserId() {
        return blackUserId;
    }

    public long getWhiteUserId() {
        return whiteUserId;
    }
}

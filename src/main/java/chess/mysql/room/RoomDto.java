package chess.mysql.room;

class RoomDto {
    private final long roomId;
    private final String roomName;
    private final long gameId;

    public RoomDto(long roomId, String roomName, long gameId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameId = gameId;
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
}

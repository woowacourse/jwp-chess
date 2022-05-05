package chess.entity;

public class RoomEntity {
    private static final int TRASH_INT = -1;
    private static final String TRASH_STRING ="";
    private final int roomId;
    private final int gameId;
    private final String roomName;
    private final String roomPassword;
    private final String status;

    public RoomEntity(int roomId, int gameId, String roomName, String roomPassword, String status) {
        this.roomId = roomId;
        this.gameId = gameId;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.status = status;
    }

    public static RoomEntity of(int gameId) {
        return new RoomEntity(TRASH_INT, gameId, TRASH_STRING, TRASH_STRING, TRASH_STRING);
    }

    public static RoomEntity of(int gameId, String status) {
        return new RoomEntity(TRASH_INT, gameId, TRASH_STRING, TRASH_STRING, status);
    }

    public static RoomEntity of(int gameId, String roomName, String roomPassword, String status) {
        return new RoomEntity(TRASH_INT, gameId, roomName, roomPassword, status);
    }

    public int getRoomId() {
        return roomId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public String getStatus() {
        return status;
    }
}

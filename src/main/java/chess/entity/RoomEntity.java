package chess.entity;

public class RoomEntity {

    private final int roomId;
    private final String name;
    private final String password;
    private final String gameState;
    private final String turn;

    public RoomEntity(final int roomId, final String name, final String password,
                      final String gameState, final String turn) {
        this.roomId = roomId;
        this.name = name;
        this.password = password;
        this.gameState = gameState;
        this.turn = turn;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGameState() {
        return gameState;
    }

    public String getTurn() {
        return turn;
    }
}

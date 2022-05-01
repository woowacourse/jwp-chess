package chess.entity;

public class Room {

    private static final String PLAYING_STATE_VALUE = "playing";

    private final int roomId;
    private final String name;
    private final String password;
    private final String gameState;
    private final String turn;

    public Room(final int roomId, final String name, final String password,
                final String gameState, final String turn) {
        this.roomId = roomId;
        this.name = name;
        this.password = password;
        this.gameState = gameState;
        this.turn = turn;
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public boolean isPlayingState() {
        return gameState.equals(PLAYING_STATE_VALUE);
    }

    public String getTurn() {
        return turn;
    }
}

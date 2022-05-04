package chess.database.entity;

import chess.domain.game.GameState;

public class GameEntity {

    private Long id;
    private final String turnColor;
    private final String state;
    private Long roomId;

    public GameEntity(Long id, String turnColor, String state, Long roomId) {
        this.id = id;
        this.turnColor = turnColor;
        this.state = state;
        this.roomId = roomId;
    }

    public GameEntity(Long id, String turnColor, String state) {
        this(id, turnColor, state, null);
    }

    public GameEntity(String turnColor, String state, Long roomId) {
        this(null, turnColor, state, roomId);
    }

    public static GameEntity from(GameState state, Long id) {
        return new GameEntity(id, state.getColor(), state.getState());
    }

    public static GameEntity fromRoomId(GameState state, Long roomId) {
        return new GameEntity(state.getColor(), state.getState(), roomId);
    }

    public Long getId() {
        return id;
    }

    public String getTurnColor() {
        return turnColor;
    }

    public String getState() {
        return state;
    }

    public Long getRoomId() {
        return roomId;
    }
}

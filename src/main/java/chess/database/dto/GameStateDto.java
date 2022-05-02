package chess.database.dto;

import chess.domain.Color;
import chess.domain.game.GameState;
import chess.domain.game.State;

public class GameStateDto {

    private final State state;
    private final Color turnColor;

    private GameStateDto(State state, Color turnColor) {
        this.state = state;
        this.turnColor = turnColor;
    }

    public static GameStateDto of(GameState gameState) {
        return new GameStateDto(gameState.getState(), gameState.getTurnColor());
    }

    public static GameStateDto of(State state, Color color) {
        return new GameStateDto(state, color);
    }

    public State getState() {
        return state;
    }

    public Color getTurnColor() {
        return turnColor;
    }
}

package chess.dto;

import chess.domain.game.GameState;

public class GameStateResponse {
    private final BoardResponse board;
    private final String state;
    private final String color;

    public GameStateResponse(BoardResponse board, String state, String color) {
        this.board = board;
        this.state = state;
        this.color = color;
    }

    public static GameStateResponse of(GameState state) {
        return new GameStateResponse(BoardResponse.of(state.getPointPieces()), state.getState(), state.getColor());
    }

    public BoardResponse getBoard() {
        return board;
    }

    public String getState() {
        return state;
    }

    public String getColor() {
        return color;
    }
}

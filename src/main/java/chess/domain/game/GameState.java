package chess.domain.game;

import chess.domain.board.piece.Color;

public enum GameState {

    WHITE_TURN(Color.WHITE),
    BLACK_TURN(Color.BLACK),
    OVER(null);

    private final Color color;

    GameState(Color color) {
        this.color = color;
    }

    public Color toColor() {
        if (this == OVER) {
            throw new IllegalArgumentException("종료된 게임입니다.");
        }
        return color;
    }
}

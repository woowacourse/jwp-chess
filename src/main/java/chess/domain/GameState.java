package chess.domain;

import java.util.Arrays;

import chess.domain.board.Board;

public enum GameState {

    END(Color.NONE),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK);

    private final Color color;

    GameState(Color color) {
        this.color = color;
    }

    public static GameState from(Board board) {
        if (board.isEnd()) {
            return END;
        }
        return Arrays.stream(values())
            .filter(each -> each.color.equals(board.getTurn()))
            .findAny()
            .orElseThrow();
    }

    public String getTurn() {
        return color.name();
    }
}

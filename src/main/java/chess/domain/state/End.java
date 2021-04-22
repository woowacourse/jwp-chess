package chess.domain.state;

import chess.domain.piece.Color;
import chess.exception.ChessException;
import chess.exception.ErrorCode;

public class End implements State {
    public static final End BLACK_WIN = new End(Color.BLACK);
    public static final End WHITE_WIN = new End(Color.WHITE);

    private final Color winner;

    private End(Color color) {
        this.winner = color;
    }

    @Override
    public Color color() {
        return winner;
    }

    @Override
    public State opposite() {
        throw new ChessException(ErrorCode.FINISHED_GAME);
    }

    @Override
    public State end() {
        throw new ChessException(ErrorCode.FINISHED_GAME);
    }

}

package chess.domain.move;

import chess.domain.board.ChessGame;
import chess.domain.board.Square;
import chess.domain.state.MoveInfo;
import chess.domain.state.MoveState;

public class MoveStateChecker {

    private static final Square DEFAULT_SQUARE = Square.of("a1");
    private final MoveStateStrategy moveStateStrategy;

    public MoveStateChecker(MoveStateStrategy moveStateStrategy) {
        this.moveStateStrategy = moveStateStrategy;
    }

    public MoveState check(ChessGame chessGame) {
        return check(chessGame, new MoveInfo(DEFAULT_SQUARE, DEFAULT_SQUARE));
    }

    public MoveState check(ChessGame chessGame, MoveInfo moveInfo) {
        return moveStateStrategy.findMoveState(chessGame, moveInfo);
    }
}

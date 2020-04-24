package chess.model.domain.move;

import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;

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
        return moveStateStrategy.getMoveState(chessGame, moveInfo);
    }
}

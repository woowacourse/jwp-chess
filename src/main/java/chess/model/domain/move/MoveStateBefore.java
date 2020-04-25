package chess.model.domain.move;

import chess.model.domain.board.ChessGame;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import util.NullChecker;

public class MoveStateBefore implements MoveStateStrategy {

    @Override
    public MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame, moveInfo);
        if (chessGame.isKingCaptured()) {
            return MoveState.KING_CAPTURED;
        }
        if (chessGame.isNotMovable(moveInfo)) {
            return findMoveStateWhenNotMovable(chessGame, moveInfo);
        }
        if (chessGame.canPromote()) {
            return MoveState.FAIL_MUST_PAWN_PROMOTION;
        }
        return MoveState.READY;
    }

    private MoveState findMoveStateWhenNotMovable(ChessGame chessGame, MoveInfo moveInfo) {
        if (chessGame.isEmptySquare(moveInfo)) {
            return MoveState.FAIL_NO_PIECE;
        }
        if (chessGame.isNotMyTurn(moveInfo)) {
            return MoveState.FAIL_NOT_ORDER;
        }
        return MoveState.FAIL_CAN_NOT_MOVE;
    }
}

package chess.model.domain.move;

import chess.model.domain.board.ChessGame;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import util.NullChecker;

public class MoveStateBefore implements MoveStateStrategy {

    @Override
    public MoveState getMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame, moveInfo);
        if (chessGame.isKingCaptured()) {
            return MoveState.KING_CAPTURED;
        }
        if (!chessGame.canMove(moveInfo)) {
            return getWhyCanMove(chessGame, moveInfo);
        }
        if (chessGame.isNeedPromotion()) {
            return MoveState.FAIL_MUST_PAWN_PROMOTION;
        }
        return MoveState.READY;
    }

    private MoveState getWhyCanMove(ChessGame chessGame, MoveInfo moveInfo) {
        if (chessGame.isNoPiece(moveInfo)) {
            return MoveState.FAIL_NO_PIECE;
        }
        if (chessGame.isNotMyTurn(moveInfo)) {
            return MoveState.FAIL_NOT_ORDER;
        }
        return MoveState.FAIL_CAN_NOT_MOVE;
    }
}

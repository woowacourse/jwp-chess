package chess.domain.move;

import chess.domain.board.ChessGame;
import chess.domain.state.MoveInfo;
import chess.domain.state.MoveState;
import util.NullChecker;

public class MoveStateBefore implements MoveStateStrategy {

    @Override
    public MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame, moveInfo);
        if (chessGame.isKingCaptured()) {
            return MoveState.KING_CAPTURED;
        }
        if (chessGame.isNotMovable(moveInfo)) {
            return findFailMoveState(chessGame, moveInfo);
        }
        if (chessGame.canPromote()) {
            return MoveState.FAIL_MUST_PAWN_PROMOTION;
        }
        return MoveState.READY;
    }

    private MoveState findFailMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        if (chessGame.isNotExistPiece(moveInfo.getSource())) {
            return MoveState.FAIL_NO_PIECE;
        }
        if (chessGame.isNotCorrectTurn(moveInfo)) {
            return MoveState.FAIL_NOT_ORDER;
        }
        return MoveState.FAIL_CAN_NOT_MOVE;
    }
}

package chess.domain.move;

import chess.domain.board.ChessGame;
import chess.domain.state.MoveInfo;
import chess.domain.state.MoveState;
import util.NullChecker;

public class MoveStateAfter implements MoveStateStrategy {

    @Override
    public MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame);
        if (chessGame.isKingCaptured()) {
            return MoveState.KING_CAPTURED;
        }
        if (chessGame.canPromote()) {
            return MoveState.SUCCESS_BUT_PAWN_PROMOTION;
        }
        return MoveState.SUCCESS;
    }
}

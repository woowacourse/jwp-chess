package chess.domain.move;

import chess.domain.board.ChessGame;
import chess.domain.state.MoveInfo;
import chess.domain.state.MoveState;
import util.NullChecker;

public class MoveStatePromotion implements MoveStateStrategy {

    @Override
    public MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame);
        if (chessGame.canPromote()) {
            return MoveState.NEEDS_PROMOTION;
        }
        return MoveState.NO_PAWN_PROMOTION;
    }
}

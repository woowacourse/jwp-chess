package chess.model.domain.move;

import chess.model.domain.board.ChessGame;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import util.NullChecker;

public class MoveStatePromotion implements MoveStateStrategy {

    @Override
    public MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo) {
        NullChecker.validateNotNull(chessGame);
        if (chessGame.isNeedPromotion()) {
            return MoveState.NEEDS_PROMOTION;
        }
        return MoveState.NO_PAWN_PROMOTION;
    }
}

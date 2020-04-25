package chess.model.domain.move;

import chess.model.domain.board.ChessBoard;
import chess.model.domain.piece.Team;

public abstract class AbstractMoveState implements MoveStateStrategy {

    public boolean isKingCaptured(ChessBoard chessBoard) {
        return chessBoard.countPieceOfKing() != Team.values().length;
    }
}

package chess.model.domain.move;

import chess.model.domain.board.ChessGame;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;

public interface MoveStateStrategy {

    MoveState getMoveState(ChessGame chessGame, MoveInfo moveInfo);

}

package chess.domain.move;

import chess.domain.board.ChessGame;
import chess.domain.state.MoveInfo;
import chess.domain.state.MoveState;

public interface MoveStateStrategy {

    MoveState findMoveState(ChessGame chessGame, MoveInfo moveInfo);

}

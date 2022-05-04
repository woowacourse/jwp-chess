package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.Result;
import chess.domain.position.Position;

public interface State {

    State start();

    State end();

    State move(Position source, Position target);

    boolean isRunning();

    boolean isFinished();

    Result winner();

    ChessBoard chessBoard();

    StateType getStateType();
}

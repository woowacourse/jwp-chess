package chess.state;

import chess.domain.Chessboard;
import chess.domain.MovingPosition;
import chess.domain.Turn;

public interface State {

    String UNSUPPORTED_STATE = "현재 상태에서 지원되지 않는 기능입니다.";

    State start();

    State move(Chessboard chessboard, MovingPosition movingPosition, Turn turn);

    State end();

    boolean isFinished();

    String getStateToString();

}

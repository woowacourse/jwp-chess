package chess.domain.state;

import chess.domain.chessboard.ChessBoard;
import chess.domain.command.GameCommand;
import java.util.Arrays;

public interface State {

    State proceed(final ChessBoard chessBoard, final GameCommand gameCommand);

    static State of(String value) {
        StateName gameState = Arrays.stream(StateName.values())
                .filter(state -> state.isSame(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("게임 진행 상태가 없습니다."));
        return gameState.getState();
    }

    boolean isFinished();

    String getValue();
}

package chess.domain.state;

import chess.domain.chessboard.Board;
import chess.domain.game.Player;

import static chess.domain.state.Command.*;

public final class Start extends Running {

    private Start() {
        super(new Board());
    }

    public static State of() {
        return new Start();
    }

    public static State initState(final String command) {
        if (!START.isUserInput(command)) {
            throw new IllegalArgumentException("[ERROR] 아직 게임이 시작되지 않았습니다.");
        }
        return new Start();
    }

    @Override
    public State proceed(final String command) {
        if (END.startsWith(command)) {
            return new End(board);
        }
        if (MOVE.startsWith(command)) {
            movePiece(command);
            return new Turn(board, Player.WHITE);
        }
        throw new IllegalArgumentException("[ERROR] 올바른 명령을 입력해주세요.");
    }

    @Override
    public Player getNextTurnPlayer() {
        return Player.WHITE;
    }
}

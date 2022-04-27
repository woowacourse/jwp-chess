package chess.state;

import chess.chessboard.Board;
import chess.chessboard.position.Position;
import java.util.List;

public abstract class Running extends Started {

    public Running(final Board board) {
        super(board);
    }

    protected void movePiece(String command) {
        final List<String> option = List.of(command.split(" "));
        final Position source = Position.from(option.get(1));
        final Position target = Position.from(option.get(2));
        checkTurn(source);
        board.move(source, target);
    }

    private void checkTurn(final Position source) {
        if (!board.checkRightTurn(getNextTurnPlayer(), source)) {
            throw new IllegalArgumentException("[ERROR] 상대편 기물은 선택 할 수 없습니다.");
        }
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public boolean isStatus() {
        return false;
    }
}

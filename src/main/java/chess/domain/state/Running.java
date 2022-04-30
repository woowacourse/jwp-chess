package chess.domain.state;

import chess.domain.chessboard.Board;
import chess.domain.chessboard.position.Position;
import java.util.List;

public abstract class Running extends Started {

    public Running(final Board board) {
        super(board);
    }

    protected void movePiece(String command) {
        final List<String> commandOption = List.of(command.split(" "));
        validateMoveCommand(commandOption);
        final Position source = Position.from(commandOption.get(1));
        final Position target = Position.from(commandOption.get(2));
        checkTurn(source);
        board.move(source, target);
    }

    private void validateMoveCommand(List<String> commandOption) {
        if (commandOption.size() != 3 || !Command.MOVE.isUserInput(commandOption.get(0))) {
            throw new IllegalArgumentException("[ERROR] 잘못된 값이 입력 되었습니다.");
        }
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

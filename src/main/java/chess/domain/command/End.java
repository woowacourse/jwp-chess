package chess.domain.command;

import chess.exception.ChessException;
import chess.exception.ErrorInformation;

public class End implements Command {
    @Override
    public Command execute(final String command) {
        throw new ChessException(ErrorInformation.ALREADY_FINISHED);
    }

    @Override
    public boolean isStart() {
        return false;
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public boolean isMove() {
        return false;
    }

    @Override
    public boolean isStatus() {
        return false;
    }
}

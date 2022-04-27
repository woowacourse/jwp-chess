package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.Command;
import chess.exception.IllegalMoveException;

public class End implements State {

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public State execute(Command command, ChessBoard chessBoard) {
        if (!command.isEnd()) {
            throw new IllegalMoveException("이미 게임이 종료되었습니다.");
        }
        return this;
    }

    @Override
    public String getTurn() {
        return "end";
    }
}

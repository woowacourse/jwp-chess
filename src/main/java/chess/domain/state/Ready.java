package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.Result;
import chess.domain.generator.EmptyBoardGenerator;
import chess.domain.position.Position;
import chess.exception.InvalidChessStateException;

public class Ready extends Started {

    public static final String ERROR_MESSAGE_GAME_NOT_START = "게임이 시작되지 않았습니다.";

    public Ready() {
        super(new ChessBoard(new EmptyBoardGenerator()));
    }

    @Override
    public State start() {
        chessBoard.init();
        return new WhiteTurn(chessBoard);
    }

    @Override
    public State end() {
        throw new InvalidChessStateException(ERROR_MESSAGE_GAME_NOT_START);
    }

    @Override
    public State move(Position source, Position target) {
        throw new InvalidChessStateException(ERROR_MESSAGE_GAME_NOT_START);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Result winner() {
        throw new InvalidChessStateException(ERROR_MESSAGE_GAME_NOT_START);
    }

    @Override
    public StateType getStateType() {
        return StateType.READY;
    }
}

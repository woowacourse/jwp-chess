package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.Color;
import chess.domain.position.Position;
import chess.exception.InvalidChessStateException;

public class WhiteTurn extends Running {

    protected WhiteTurn(ChessBoard chessBoard) {
        super(chessBoard);
    }

    @Override
    public State move(Position source, Position target) {
        if (chessBoard.isTurn(source, Color.BLACK)) {
            throw new InvalidChessStateException("black 진영의 차례가 아닙니다.");
        }

        chessBoard.move(source, target);

        if (chessBoard.isFinished()) {
            return new WhiteWin(chessBoard);
        }

        return new BlackTurn(chessBoard);
    }

    @Override
    public StateType getStateType() {
        return StateType.WHITE_TURN;
    }
}

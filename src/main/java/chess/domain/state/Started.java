package chess.domain.state;

import chess.domain.ChessBoard;

public abstract class Started implements State {

    protected final String INVALID_CHESS_STATE_EXCEPTION = "현재 상태에서 지원하지 않는 기능입니다.";

    protected final ChessBoard chessBoard;

    protected Started(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public final ChessBoard chessBoard() {
        return chessBoard;
    }
}

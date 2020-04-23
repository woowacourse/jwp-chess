package chess.model.domain.board;

import chess.model.domain.piece.Piece;
import java.util.Map;

public class BoardInitialTestUse implements BoardInitialization {

    private final Map<Square, Piece> chessBoard;

    public BoardInitialTestUse(Map<Square, Piece> chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public Map<Square, Piece> getInitialize() {
        return chessBoard;
    }
}

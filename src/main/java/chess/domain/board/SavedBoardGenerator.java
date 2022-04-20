package chess.domain.board;

import chess.domain.piece.Piece;
import chess.domain.position.Square;

import java.util.Map;

public class SavedBoardGenerator implements BoardGenerator{
    private final Map<Square, Piece> board;

    public SavedBoardGenerator(Map<Square, Piece> board) {
        this.board = board;
    }

    @Override
    public Map<Square, Piece> generate() {
        return board;
    }
}

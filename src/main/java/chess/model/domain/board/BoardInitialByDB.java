package chess.model.domain.board;

import chess.model.domain.piece.Piece;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardInitialByDB implements BoardInitialization {

    private final Map<Square, Piece> board;

    public BoardInitialByDB(Map<Square, Piece> board) {
        this.board = Collections.unmodifiableMap(board);
    }

    @Override
    public Map<Square, Piece> getInitialize() {
        return new HashMap<>(board);
    }
}

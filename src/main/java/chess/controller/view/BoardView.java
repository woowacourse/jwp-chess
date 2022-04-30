package chess.controller.view;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardView {

    private final Map<String, Piece> boardView;

    public BoardView(Map<String, Piece> boardView) {
        this.boardView = Collections.unmodifiableMap(boardView);
    }

    public static BoardView of(Pieces pieces) {
        Map<String, Piece> boardView = new HashMap<>();
        for (Piece piece : pieces.getPieces()) {
            boardView.put(piece.getPosition().getPosition(), piece);
        }
        return new BoardView(boardView);
    }

    public Map<String, Piece> getBoardView() {
        return boardView;
    }
}

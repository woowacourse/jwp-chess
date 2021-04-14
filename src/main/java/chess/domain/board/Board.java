package chess.domain.board;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Board {

    private final Map<Position, Piece> chessBoard;

    public Board() {
        chessBoard = BoardInitializer.initializeBoard();
    }

    public Board(final Map<Position, Piece> chessBoard) {
        this.chessBoard = new TreeMap(chessBoard);
    }

    public Map<Position, Piece> chessBoard() {
        return Collections.unmodifiableMap(chessBoard);
    }
}

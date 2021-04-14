package chess.domain.board;

import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BishopInitializer implements LocationInitializer {

    private static final List<String> FILES = Arrays.asList("c", "f");
    private static final List<String> RANKS_WHITE = Collections.singletonList("1");
    private static final List<String> RANKS_BLACK = Collections.singletonList("8");

    @Override
    public Map<Position, Piece> initialize() {
        final Map<Position, Piece> chessBoard = new HashMap<>();
        for (final String file : FILES) {
            RANKS_BLACK.forEach(
                rank -> chessBoard.put(new Position(file, rank), new Bishop(Color.BLACK, new Position(file, rank))));
            RANKS_WHITE.forEach(
                rank -> chessBoard.put(new Position(file, rank), new Bishop(Color.WHITE, new Position(file, rank))));
        }
        return chessBoard;
    }
}

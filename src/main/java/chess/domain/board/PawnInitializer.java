package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PawnInitializer implements LocationInitializer {

    private static final List<String> FILES = File.fileSymbols();
    private static final List<String> RANKS_WHITE = Collections.singletonList("7");
    private static final List<String> RANKS_BLACK = Collections.singletonList("2");

    @Override
    public Map<Position, Piece> initialize() {
        final Map<Position, Piece> chessBoard = new HashMap<>();
        for (final String file : FILES) {
            RANKS_WHITE.forEach(
                rank -> chessBoard.put(new Position(file, rank), new Pawn(Color.WHITE, new Position(file, rank))));
            RANKS_BLACK.forEach(
                rank -> chessBoard.put(new Position(file, rank), new Pawn(Color.BLACK, new Position(file, rank))));
        }
        return chessBoard;
    }
}

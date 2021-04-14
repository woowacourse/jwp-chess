package chess.domain.piece.factory;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.position.Position;

import java.util.*;

public final class QueenInitializer implements LocationInitializer {

    private static final List<String> FILES = Collections.singletonList("d");
    private static final List<String> RANKS_WHITE = Collections.singletonList("1");
    private static final List<String> RANKS_BLACK = Collections.singletonList("8");

    @Override
    public List<Piece> whiteInitialize() {
        List<Piece> pieces = new ArrayList<>();
        for (final String file : FILES) {
            RANKS_WHITE.forEach(
                    rank -> pieces.add(new Queen(Color.WHITE, new Position(file, rank))));
        }
        return pieces;
    }

    @Override
    public List<Piece> blackInitialize() {
        List<Piece> pieces = new ArrayList<>();
        for (final String file : FILES) {
            RANKS_BLACK.forEach(
                    rank -> pieces.add(new Queen(Color.WHITE, new Position(file, rank))));
        }
        return pieces;
    }
}

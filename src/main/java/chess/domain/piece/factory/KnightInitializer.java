package chess.domain.piece.factory;

import chess.domain.piece.Color;
import chess.domain.piece.Knight;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class KnightInitializer implements LocationInitializer {

    private static final List<String> FILES = Arrays.asList("b", "g");
    private static final List<String> RANKS_WHITE = Collections.singletonList("1");
    private static final List<String> RANKS_BLACK = Collections.singletonList("8");

    @Override
    public List<Piece> whiteInitialize() {
        List<Piece> pieces = new ArrayList<>();
        for (final String file : FILES) {
            RANKS_WHITE.forEach(
                    rank -> pieces.add(new Knight(Color.WHITE, new Position(file, rank))));
        }
        return pieces;
    }

    @Override
    public List<Piece> blackInitialize() {
        List<Piece> pieces = new ArrayList<>();
        for (final String file : FILES) {
            RANKS_BLACK.forEach(
                    rank -> pieces.add(new Knight(Color.BLACK, new Position(file, rank))));
        }
        return pieces;
    }
}

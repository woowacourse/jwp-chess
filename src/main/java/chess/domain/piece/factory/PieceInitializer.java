package chess.domain.piece.factory;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceInitializer {
    private static final List<LocationInitializer> locationInitializers;

    static {
        locationInitializers = Arrays.asList(new PawnInitializer(), new RookInitializer(), new KingInitializer(),
                new QueenInitializer(), new BishopInitializer(), new KnightInitializer());
    }

    public static Pieces whitePieces() {
        final List<Piece> pieces = new ArrayList<>();

        for (LocationInitializer locationInitializer : locationInitializers) {
            pieces.addAll(locationInitializer.whiteInitialize());
        }

        return new Pieces(pieces);
    }

    public static Pieces blackPieces() {
        final List<Piece> pieces = new ArrayList<>();

        for (LocationInitializer locationInitializer : locationInitializers) {
            pieces.addAll(locationInitializer.blackInitialize());
        }

        return new Pieces(pieces);
    }
}

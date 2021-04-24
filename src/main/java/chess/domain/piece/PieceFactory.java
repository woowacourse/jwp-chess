package chess.domain.piece;

import static chess.domain.piece.Color.*;

import java.util.Arrays;
import java.util.List;

public class PieceFactory {

    private static final List<Piece> pieces = Arrays.asList(
        new Pawn(BLACK), new Pawn(WHITE),
        new Knight(BLACK), new Knight(WHITE),
        new Bishop(BLACK), new Bishop(WHITE),
        new King(BLACK), new King(WHITE),
        new Queen(BLACK), new Queen(WHITE),
        new Rook(BLACK), new Rook(WHITE),
        new EmptyPiece()
    );

    public static Piece of(String name) {
        return pieces.stream()
            .filter(piece -> piece.getName().equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
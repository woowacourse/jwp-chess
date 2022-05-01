package chess.domain.piece;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import java.util.ArrayList;
import java.util.List;

public class PieceStorage {

    private static final List<Piece> pieces = new ArrayList<>();

    static {
        pieces.add(new King(BLACK));
        pieces.add(new King(WHITE));
        pieces.add(new Queen(BLACK));
        pieces.add(new Queen(WHITE));
        pieces.add(new Bishop(BLACK));
        pieces.add(new Bishop(WHITE));
        pieces.add(new Knight(BLACK));
        pieces.add(new Knight(WHITE));
        pieces.add(new Rook(BLACK));
        pieces.add(new Rook(WHITE));
        pieces.add(new Pawn(BLACK));
        pieces.add(new Pawn(WHITE));
    }

    public static Piece valueOf(String name, String color) {
        return pieces.stream()
            .filter(piece -> piece.getName().equals(name))
            .filter(piece -> piece.getColor().equals(color))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체스말입니다."));
    }

    public static Piece valueOf(Name name, Color color) {
        return pieces.stream()
            .filter(piece -> piece.getName() == name)
            .filter(piece -> piece.getColor() == color)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체스말입니다."));
    }

}

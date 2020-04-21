package wooteco.chess.domain.piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceRepository {
    private static final List<Piece> pieces = new ArrayList<>();

    static {
        pieces.add(new King(Team.BLACK));
        pieces.add(new King(Team.WHITE));
        pieces.add(new Queen(Team.BLACK));
        pieces.add(new Queen(Team.WHITE));
        pieces.add(new Rook(Team.BLACK));
        pieces.add(new Rook(Team.WHITE));
        pieces.add(new Bishop(Team.BLACK));
        pieces.add(new Bishop(Team.WHITE));
        pieces.add(new Knight(Team.BLACK));
        pieces.add(new Knight(Team.WHITE));
        pieces.add(new Pawn(Team.BLACK));
        pieces.add(new Pawn(Team.WHITE));
    }

    public static List<Piece> pieces() {
        return Collections.unmodifiableList(pieces);
    }
}

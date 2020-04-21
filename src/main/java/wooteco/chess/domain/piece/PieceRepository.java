package wooteco.chess.domain.piece;

import java.util.ArrayList;
import java.util.List;

public class PieceRepository {
    private static final List<Piece> pieces = new ArrayList<>();

    static {
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(Team.BLACK));
            pieces.add(new Pawn(Team.WHITE));
        }
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
    }
}

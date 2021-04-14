package chess.domain.position;

import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Source {
    private final Piece piece;

    public Source(final Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece move(final Target target) {
        return piece.move(target);
    }

    public List<Integer> subtract(final Target target) {
        List<Integer> result = new ArrayList<>();
        result.add(piece.position().file().value() - target.getPiece().position().file().value());
        result.add(piece.position().rank().value() - target.getPiece().position().rank().value());
        return result;
    }

    public boolean canMove(final Target target) {
        return piece.canMove(target);
    }

    public boolean isBlack() {
        return piece.color().isBlack();
    }
}

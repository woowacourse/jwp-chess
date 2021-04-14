package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Pieces {
    private final List<Piece> pieces;

    public Pieces(final List<Piece> pieces) {
        this.pieces = new ArrayList<>(pieces);
    }

    public List<Piece> unwrap() {
        return Collections.unmodifiableList(pieces);
    }

    public boolean canMove(final Source source, final Target target) {
        return source.canMove(target);
    }

    public void move(final Source source, final Target target) {
        Piece movedPiece = source.move(target);
        remove(source.getPiece().position());
        pieces.add(movedPiece);
    }

    public Optional<Piece> findPiece(final Position position) {
        return this.pieces.stream()
                .filter(piece -> piece.isSamePosition(position))
                .findAny();
    }

    public void remove(final Position position) {
        if (findPiece(position).isPresent()) {
            this.pieces.remove(findPiece(position).get());
        }
    }

    public List<Piece> pieces() {
        return Collections.unmodifiableList(pieces);
    }
}

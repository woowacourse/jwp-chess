package chess.domain.piece;

import chess.domain.position.Source;
import chess.domain.position.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

/*    public void move(final Source source, final Target target) {
        source.move(target);
    }*/
}

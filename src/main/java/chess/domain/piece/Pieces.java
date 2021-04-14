package chess.domain.piece;

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
}

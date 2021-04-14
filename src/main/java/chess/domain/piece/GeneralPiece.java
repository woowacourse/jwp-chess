package chess.domain.piece;

import chess.domain.position.Position;

public abstract class GeneralPiece extends Piece {

    public GeneralPiece(final Color color, final String initialName, final Position position) {
        super(color, initialName, position);
    }
}

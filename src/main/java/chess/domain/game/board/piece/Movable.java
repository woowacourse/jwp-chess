package chess.domain.game.board.piece;

import chess.domain.game.board.piece.location.Location;

public interface Movable {

    boolean isMovable(final Location target);

}

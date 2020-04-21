package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public interface Piece {
    boolean isMovable(final Path path);
    boolean isInitialPosition(final Position position);
}

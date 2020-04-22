package wooteco.chess.domain.piece.movable;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.positions.Positions;

import java.util.List;

public interface Movable {
    Positions findMovablePositions(Position position, List<Piece> pieces, Color color);
}

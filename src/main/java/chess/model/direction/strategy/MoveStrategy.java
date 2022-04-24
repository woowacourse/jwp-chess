package chess.model.direction.strategy;

import java.util.List;
import java.util.Map;

import chess.model.piece.Piece;
import chess.model.position.Position;

public interface MoveStrategy {

    List<Position> searchMovablePositions(Position source, Map<Position, Piece> board);
}

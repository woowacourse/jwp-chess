package chess.domain.piece.movementcondition;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;

public interface MovementCondition {

    boolean isPossibleMovement(Position from, Position to, Map<Position, Piece> board);
}

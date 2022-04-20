package springchess.model.piece;

import springchess.model.board.ConsoleBoard;
import springchess.model.square.Square;

import java.util.Collections;
import java.util.List;

public abstract class PointMovingPiece extends Piece {

    protected PointMovingPiece(Team team) {
        super(team);
    }

    protected PointMovingPiece(int id, Team team, int squareId) {
        super(id, team, squareId);
    }

    @Override
    public boolean movable(Square source, Square target) {
        return getDirection().stream()
                .anyMatch(direction -> source.findLocation(direction, target));
    }

    public boolean canMoveWithoutObstacle(ConsoleBoard consoleBoard, Square source, Square target) {
        Piece targetPiece = consoleBoard.get(target);
        return isNotAlly(targetPiece);
    }

    @Override
    public List<Square> getRoute(Square source, Square target) {
        return Collections.emptyList();
    }
}

package spring.chess.piece.type.movable;

import spring.chess.board.Route;

public class RookPieceMovable implements PieceMovable {
    @Override
    public boolean canMove(Route route) {
        return route.isStraight() && hasNotObstacle(route);
    }
}

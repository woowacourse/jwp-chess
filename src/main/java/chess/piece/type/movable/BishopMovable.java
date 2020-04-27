package chess.piece.type.movable;

import chess.board.Route;

public class BishopMovable implements PieceMovable {
    @Override
    public boolean canMove(Route route) {
        return route.isDiagonal() && hasNotObstacle(route);
    }
}

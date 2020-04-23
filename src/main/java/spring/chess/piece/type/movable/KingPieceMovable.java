package spring.chess.piece.type.movable;

import spring.chess.board.Route;
import spring.chess.location.Location;

import static java.lang.Math.abs;

public class KingPieceMovable implements PieceMovable {
    private static final int KING_RANGE = 1;

    @Override
    public boolean canMove(Route route) {
        return isKingRange(route.getNow(), route.getDestination());
    }

    private boolean isKingRange(Location now, Location destination) {
        boolean canRowMove =
                abs(now.getRowValue() - destination.getRowValue()) <= KING_RANGE;
        boolean canColMove =
                abs(now.getColValue() - destination.getColValue()) <= KING_RANGE;

        return canRowMove && canColMove;
    }
}

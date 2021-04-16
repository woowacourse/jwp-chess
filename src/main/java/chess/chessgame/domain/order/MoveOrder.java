package chess.chessgame.domain.order;

import chess.chessgame.domain.board.Square;
import chess.chessgame.domain.position.Direction;

import java.util.List;

public class MoveOrder {
    private final Direction direction;
    private final List<Square> route;
    private final Square from;
    private final Square to;

    public MoveOrder(Direction direction, List<Square> route, Square from, Square to) {
        this.direction = direction;
        this.route = route;
        this.from = from;
        this.to = to;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Square> getRoute() {
        return route;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }
}

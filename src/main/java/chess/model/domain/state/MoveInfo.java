package chess.model.domain.state;

import chess.model.domain.board.Square;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import util.NullChecker;

public class MoveInfo {

    private final Map<MoveOrder, Square> squares;

    public MoveInfo(String before, String after) {
        this(Square.of(before), Square.of(after));
    }

    public MoveInfo(Square squareBefore, Square squareAfter) {
        NullChecker.validateNotNull(squareBefore, squareAfter);

        Map<MoveOrder, Square> squares = new HashMap<>();
        squares.put(MoveOrder.FROM, squareBefore);
        squares.put(MoveOrder.TO, squareAfter);

        this.squares = Collections.unmodifiableMap(squares);
    }

    public Square get(MoveOrder moveOrder) {
        return squares.get(moveOrder);
    }

    public int calculateRankDistance() {
        Square squareFrom = squares.get(MoveOrder.FROM);
        Square squareTo = squares.get(MoveOrder.TO);
        return squareFrom.calculateRankDistance(squareTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveInfo that = (MoveInfo) o;
        return Objects.equals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(squares);
    }
}

package chess.domain.state;

import chess.domain.board.Square;
import java.util.Objects;
import util.NullChecker;

public class MoveInfo {

    private final Square source;
    private final Square target;

    public MoveInfo(Square source, Square target) {
        NullChecker.validateNotNull(source, target);
        this.source = source;
        this.target = target;
    }

    public MoveInfo(String source, String target) {
        this(Square.of(source), Square.of(target));
    }

    public Square getSource() {
        return source;
    }

    public Square getTarget() {
        return target;
    }

    public int calculateRankDistance() {
        return source.calculateRankDistance(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveInfo moveInfo = (MoveInfo) o;
        return Objects.equals(source, moveInfo.source) &&
            Objects.equals(target, moveInfo.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}

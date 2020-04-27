package wooteco.chess.domain;

import wooteco.chess.domain.position.Position;

public class MoveParameter {
    Position source;
    Position target;

    private MoveParameter(Position source, Position target) {
        validatePosition(source, target);
        this.source = source;
        this.target = target;
    }

    public static MoveParameter of(Position source, Position target) {
        return new MoveParameter(source, target);
    }

    private static void validatePosition(Position source, Position target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("같은 지점으로의 이동은 불가능 합니다.");
        }
    }

    public Position getSource() {
        return source;
    }

    public Position getTarget() {
        return target;
    }
}
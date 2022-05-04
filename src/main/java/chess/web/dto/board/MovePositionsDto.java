package chess.web.dto.board;

import chess.domain.position.Position;

public class MovePositionsDto {

    private final Position source;
    private final Position target;

    public MovePositionsDto(Position source, Position target) {
        this.source = source;
        this.target = target;
    }

    public Position getSource() {
        return source;
    }

    public Position getTarget() {
        return target;
    }
}

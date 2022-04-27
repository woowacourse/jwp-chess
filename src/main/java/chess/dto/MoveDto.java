package chess.dto;

import chess.domain.position.Position;

public class MoveDto {

    private String start;
    private String target;

    protected MoveDto() {
    }

    public MoveDto(String start, String target) {
        this.start = start;
        this.target = target;
    }

    public Position getStart() {
        return Position.of(start);
    }

    public Position getTarget() {
        return Position.of(target);
    }
}

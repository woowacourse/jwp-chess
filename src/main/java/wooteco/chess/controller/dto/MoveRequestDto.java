package wooteco.chess.controller.dto;

import wooteco.chess.domain.MoveParameter;
import wooteco.chess.domain.position.Position;

public class MoveRequestDto {

    private String source;
    private String target;

    public MoveRequestDto() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public MoveParameter generateMoveParameter() {
        Position source = Position.of(this.source);
        Position target = Position.of(this.target);
        return MoveParameter.of(source, target);
    }
}

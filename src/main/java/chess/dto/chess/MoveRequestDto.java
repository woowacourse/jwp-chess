package chess.dto.chess;

import chess.domain.team.Team;

public class MoveRequestDto {

    private final String source;
    private final String target;
    private final Team color;

    public MoveRequestDto(final String source, final String target, final Team color) {
        this.source = source;
        this.target = target;
        this.color = color;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Team getColor() {
        return color;
    }

}

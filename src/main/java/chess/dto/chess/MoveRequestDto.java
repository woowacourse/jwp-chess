package chess.dto.chess;

import chess.domain.team.Team;

public class MoveRequestDto {

    private final Team color;
    private final String source;
    private final String target;

    public MoveRequestDto(final Team color, final String source, final String target) {
        this.color = color;
        this.source = source;
        this.target = target;
    }

    public Team getColor() {
        return color;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}

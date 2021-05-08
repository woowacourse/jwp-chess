package chess.web.dto.game.move;

import chess.domain.game.team.Team;

public class MoveResponseDto {

    private String source;
    private String target;
    private Team color;
    private boolean finished;

    public MoveResponseDto() {
    }

    public MoveResponseDto(final String source, final String target, final Team color,
        final boolean finished) {

        this.source = source;
        this.target = target;
        this.color = color;
        this.finished = finished;
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

    public boolean isFinished() {
        return finished;
    }

}

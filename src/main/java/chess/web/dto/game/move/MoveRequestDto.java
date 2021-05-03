package chess.web.dto.game.move;

import chess.domain.game.team.Team;

public class MoveRequestDto {

    private String source;
    private String target;
    private Team color;

    public MoveRequestDto() {
    }

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

    public void setSource(final String source) {
        this.source = source;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public void setColor(final Team color) {
        this.color = color;
    }
}

package chess.dto;

import chess.domain.command.MoveCommand;

public class MoveCommandDto {

    private String source;
    private String target;

    public MoveCommandDto() {
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public MoveCommand toEntity() {
        return MoveCommand.of(source, target);
    }
}

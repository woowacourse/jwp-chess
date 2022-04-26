package chess.dto;

import chess.domain.command.MoveCommand;

public class MoveCommandDto {

    private String source;
    private String target;

    public MoveCommandDto() {
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

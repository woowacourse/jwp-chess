package dto;

import chess.domain.command.MoveCommand;

public class MoveCommandDto {
    private final String source;
    private final String target;

    public MoveCommandDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public MoveCommand toEntity() {
        return new MoveCommand(source, target);
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}

package chess.dto;

import chess.domain.command.MoveCommand;

public class MoveDto {

    private String source;
    private String target;

    public MoveDto() {
    }

    public MoveDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public MoveCommand toEntity() {
        return new MoveCommand(source, target);
    }

}

package chess.controller.request;

import chess.domain.command.MoveCommand;

public class MoveRequest {

    private String source;
    private String target;

    public MoveRequest() {
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

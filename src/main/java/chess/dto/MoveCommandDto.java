package chess.dto;

import chess.domain.command.MoveCommand;

public class MoveCommandDto {
    private String source;
    private String target;
    private String gameId;

    public MoveCommandDto(){
    }

    public MoveCommandDto(String source, String target, String gameId) {
        this.source = source;
        this.target = target;
        this.gameId = gameId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getGameId() {
        return gameId;
    }

    public MoveCommand toEntity() {
        return new MoveCommand(source,target);
    }
}

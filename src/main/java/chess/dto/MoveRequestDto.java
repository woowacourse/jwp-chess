package chess.dto;

import java.beans.ConstructorProperties;

public class MoveRequestDto {
    private final String command;

    @ConstructorProperties({"command"})
    public MoveRequestDto(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("move_command")
public class MoveCommand {
    @Id
    private Long commandId;
    private String command;

    public MoveCommand() {
    }

    public MoveCommand(String command) {
        this.command = command;
    }

    public Long getCommandId() {
        return commandId;
    }

    public String getCommand() {
        return command;
    }
}
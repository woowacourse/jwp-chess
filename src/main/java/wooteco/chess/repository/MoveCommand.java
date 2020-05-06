package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("move_command")
public class MoveCommand {
    @Id
    private Long commandId;
    private String source;
    private String target;

    protected MoveCommand() {
    }

    public MoveCommand(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public Long getCommandId() {
        return commandId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
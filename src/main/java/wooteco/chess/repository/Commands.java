package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Commands")
public class Commands {
    @Id
    @Column("CommandID")
    private Long commandId;
    @Column("Command")
    private String command;

    public Commands(String command) {
        this.command = command;
    }

    public String get() {
        return command;
    }
}
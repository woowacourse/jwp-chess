package wooteco.chess.dto;

import org.springframework.data.annotation.Id;
public class Commands {
    @Id
    private Long id;
    private String command;

    public Commands(String command) {
        this.command = command;
    }

    public String get() {
        return command;
    }
}
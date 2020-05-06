package wooteco.chess.dto;

import org.springframework.data.annotation.Id;

public class Commands {
    @Id
    private Long id;
    private Long roomNumber;
    private String command;

    public Commands() {
    }

    public Commands(Long roomNumber, String command) {
        this.roomNumber = roomNumber;
        this.command = command;
    }

    public Commands(String command) {
        this.command = command;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public Long getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }
}
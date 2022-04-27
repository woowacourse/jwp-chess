package chess.controller.dto;

import chess.domain.GameState;

public class GameDto {

    private int id;
    private String name;

    public GameDto(){}

    public GameDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

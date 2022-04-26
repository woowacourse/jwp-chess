package chess.controller.dto;

import chess.domain.GameState;

public class GameDto {

    private int id;
    private GameState gameState;

    public GameDto(){}

    public GameDto(int id, GameState state) {
        this.id = id;
        this.gameState = state;
    }

    public int getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }
}

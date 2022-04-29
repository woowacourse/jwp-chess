package chess.controller.dto;

import chess.entity.RoomEntity;

public class RoomResponse {

    private final int id;
    private final String name;
    private final String gameStatus;
    private final String currentTurn;

    public RoomResponse(RoomEntity roomEntity) {
        this.id = roomEntity.getId();
        this.name = roomEntity.getName();
        this.gameStatus = roomEntity.getGameStatus();
        this.currentTurn = roomEntity.getCurrentTurn();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}

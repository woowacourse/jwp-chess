package chess.dao.dto;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;

public class RoomSaveDto {

    private final String name;
    private final String password;
    private final String gameStatus;
    private final String currentTurn;

    public RoomSaveDto(final String name, final String password, final GameStatus gameStatus, final Color currentTurn) {
        this.name = name;
        this.password = password;
        this.gameStatus = gameStatus.getValue();
        this.currentTurn = currentTurn.getValue();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}

package chess.dao.dto;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;

public class RoomUpdateDto {

    private final int id;
    private final String gameStatus;
    private final String CurrentTurn;

    public RoomUpdateDto(final int id, final GameStatus gameStatus, final Color CurrentTurn) {
        this.id = id;
        this.gameStatus = gameStatus.getValue();
        this.CurrentTurn = CurrentTurn.getValue();
    }

    public int getId() {
        return id;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getCurrentTurn() {
        return CurrentTurn;
    }
}

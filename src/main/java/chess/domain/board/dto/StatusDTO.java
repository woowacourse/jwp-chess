package chess.domain.board.dto;

import chess.domain.gameRoom.GameStatus;

public final class StatusDTO {

    private final GameStatus gameStatus;

    public StatusDTO(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}

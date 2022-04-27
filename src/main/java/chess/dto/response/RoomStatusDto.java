package chess.dto.response;

import chess.domain.GameStatus;

public class RoomStatusDto {

    private final String name;
    private final GameStatus gameStatus;

    private RoomStatusDto(final String name, final GameStatus gameStatus) {
        this.name = name;
        this.gameStatus = gameStatus;
    }

    public static RoomStatusDto of(final String name, final String gameStatus) {
        return new RoomStatusDto(
                name,
                GameStatus.from(gameStatus)
        );
    }

    public String getName() {
        return name;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}

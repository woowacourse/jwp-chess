package chess.domain.game;

import chess.domain.game.status.End;
import chess.domain.game.status.GameStatus;
import chess.domain.game.status.Playing;
import chess.domain.game.status.Ready;

public enum Status {
    READY("READY", new Ready()),
    PLAYING("PLAYING", new Playing()),
    END("END", new End());

    private final String name;
    private final GameStatus convertToGameStatus;

    Status(String name, GameStatus convertToGameStatus) {
        this.name = name;
        this.convertToGameStatus = convertToGameStatus;
    }

    public String getName() {
        return name;
    }

    public GameStatus convertToGameStatus() {
        return convertToGameStatus;
    }
}

package chess.domain.db;

import chess.util.DateTimeConvertUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Game {

    private String gameId;
    private String lastTeam;
    private LocalDateTime createdAt;

    public Game(String gameId, String lastTeam, Timestamp createdAt) {
        this.gameId = gameId;
        this.lastTeam = lastTeam;
        this.createdAt = DateTimeConvertUtils.toLocalDateTimeFrom(createdAt);
    }

    public Game(String gameId, String lastTeam, LocalDateTime createdAt) {
        this.gameId = gameId;
        this.lastTeam = lastTeam;
        this.createdAt = createdAt;
    }

    public String getGameId() {
        return gameId;
    }

    public String getLastTeam() {
        return lastTeam;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

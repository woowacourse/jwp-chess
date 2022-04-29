package chess.domain.db;

import chess.util.DateTimeConvertUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Game {

    private String gameId;
    private String lastTeam;
    private LocalDateTime createAt;

    public Game(String gameId, String lastTeam, Timestamp createAt) {
        this.gameId = gameId;
        this.lastTeam = lastTeam;
        this.createAt = DateTimeConvertUtils.toLocalDateTimeFrom(createAt);
    }

    public Game(String gameId, String lastTeam, LocalDateTime createAt) {
        this.gameId = gameId;
        this.lastTeam = lastTeam;
        this.createAt = createAt;
    }

    public String getGameId() {
        return gameId;
    }

    public String getLastTeam() {
        return lastTeam;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}

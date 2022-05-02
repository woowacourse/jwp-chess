package chess.domain.entity;

import chess.util.DateTimeConvertUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Game {

    private String gameId;
    private String lastTeam;
    private String roomName;
    private String roomEncryptedPassword;
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

    public Game(String gameId,
                String roomName,
                String roomEncryptedPassword,
                String lastTeam,
                Timestamp createdAt) {
        this.gameId = gameId;
        this.roomName = roomName;
        this.roomEncryptedPassword = roomEncryptedPassword;
        this.lastTeam = lastTeam;
        this.createdAt = DateTimeConvertUtils.toLocalDateTimeFrom(createdAt);
    }

    public String getGameId() {
        return gameId;
    }

    public String getLastTeam() {
        return lastTeam;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomEncryptedPassword() {
        return roomEncryptedPassword;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", lastTeam='" + lastTeam + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomEncryptedPassword='" + roomEncryptedPassword + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

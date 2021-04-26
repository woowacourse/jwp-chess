package chess.entity;

import chess.domain.piece.Color;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Chess {

    private final String id;
    private final String name;
    private final LocalDateTime createdDate;
    private Color winnerColor;
    private String whitePlayerId;
    private String blackPlayerId;
    private boolean isRunning;

    public Chess(final String name, final String whitePlayerId) {
        this(UUID.randomUUID().toString(), name, Color.NOTHING, whitePlayerId, null,  true, LocalDateTime.now());
    }

    public Chess(final String id, final String name, final Color winnerColor, final String whitePlayerId, final String blackPlayerId,  final boolean isRunning,
                 final LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.winnerColor = winnerColor;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.isRunning = isRunning;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getWinnerColor() {
        return winnerColor;
    }

    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void changeWinnerColor(final Color color) {
        this.winnerColor = color;
    }

    public void changeRunning(final Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void changeBlackPlayerIn(final String id) {
        this.blackPlayerId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Chess chess = (Chess) o;
        return Objects.equals(id, chess.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

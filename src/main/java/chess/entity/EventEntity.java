package chess.entity;

import chess.domain.event.EventType;
import java.util.Objects;

public class EventEntity {

    private final int gameId;
    private final EventType type;
    private final String description;

    public EventEntity(int gameId, EventType type, String description) {
        this.gameId = gameId;
        this.type = type;
        this.description = description;
    }

    public int getGameId() {
        return gameId;
    }

    public String getType() {
        return type.name();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventEntity that = (EventEntity) o;
        return gameId == that.gameId
                && Objects.equals(type, that.type)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, type, description);
    }

    @Override
    public String toString() {
        return "EventEntity{" +
                "gameId=" + gameId +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

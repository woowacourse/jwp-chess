package chess.domain.event;

import chess.entity.EventEntity;

public abstract class Event {

    private final EventType type;
    private final String description;

    protected Event(EventType type, String description) {
        this.type = type;
        this.description = description;
    }

    public static Event of(String type, String description) {
        EventType eventType = EventType.valueOf(type);
        if (eventType == EventType.MOVE) {
            return new MoveEvent(description);
        }
        return new InitEvent();
    }

    public abstract boolean isInit();

    public abstract boolean isMove();

    public abstract MoveRoute toMoveRoute();

    public EventEntity toEntityOf(int gameId) {
        return new EventEntity(gameId, type, description);
    }
}

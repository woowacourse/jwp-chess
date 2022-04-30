package chess.domain.event;

import chess.entity.EventEntity;

public abstract class Event {

    public static Event of(EventEntity eventEntity) {
        EventType eventType = EventType.valueOf(eventEntity.getType());
        if (eventType == EventType.MOVE) {
            return new MoveEvent(eventEntity.getDescription());
        }
        return new InitEvent();
    }

    public abstract boolean isInit();

    public abstract boolean isMove();

    public abstract MoveRoute toMoveRoute();

    public abstract String getType();

    public abstract String getDescription();
}

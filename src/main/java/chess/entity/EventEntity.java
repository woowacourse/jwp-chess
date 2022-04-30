package chess.entity;

import chess.domain.event.Event;

public class EventEntity {

    private final String type;
    private final String description;

    public EventEntity(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public static EventEntity of(Event event) {
        return new EventEntity(event.getType(), event.getDescription());
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

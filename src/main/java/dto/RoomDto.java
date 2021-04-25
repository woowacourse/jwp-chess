package dto;

import chess.domain.room.Room;

public class RoomDto {
    private final Long id;
    private final String name;
    private final Long gameId;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.gameId = room.getGameId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getGameId() {
        return gameId;
    }
}

package dto;

import chess.domain.Room;

public class RoomDto {
    private Long id;
    private String name;
    private Long gameId;

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

package dto;

import chess.domain.room.Room;

public class RoomDto {
    private Long id;
    private String name;
    private Long gameId;
    private boolean isFull;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.gameId = room.getGameId();
        this.isFull = !room.enterable();
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

    public boolean isFull() {
        return isFull;
    }
}

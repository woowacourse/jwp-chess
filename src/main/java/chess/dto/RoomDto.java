package chess.dto;

import chess.entity.Room;

public class RoomDto {
    private final long id;
    private final String turn;
    private final String name;

    private RoomDto(Room room) {
        this.id = room.getId();
        this.turn = room.getTurn();
        this.name = room.getName();
    }

    public static RoomDto from(Room room) {
        return new RoomDto(room);
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }
}

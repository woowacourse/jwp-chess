package chess.domain.dto;

import chess.domain.game.Room;

public class RoomDto {

    private long id;
    private String name;

    public RoomDto() {
    }

    public RoomDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoomDto from(Room room) {
        return new RoomDto(room.getId(), room.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

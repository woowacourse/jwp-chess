package chess.dto;

import chess.domain.Room;

public class RoomResponseDto {

    private final long id;
    private final String name;

    private RoomResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoomResponseDto from(Room room) {
        return new RoomResponseDto(room.getId(), room.getName());
    }

    public static RoomResponseDto of(long id, Room room) {
        return new RoomResponseDto(id, room.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

package chess.dto;

import chess.domain.room.Room;

public class RoomDto {

    private final long id;
    private final String title;

    public static RoomDto toDto(Room room) {
        return new RoomDto(room.getId(), room.getTitle());
    }

    public RoomDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}

package chess.dto;

import chess.domain.room.Room;

public class RoomResponseDto {

    private long id;
    private String title;

    public RoomResponseDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static RoomResponseDto toDto(Room room) {
        return new RoomResponseDto(room.getId(), room.getTitle());
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}

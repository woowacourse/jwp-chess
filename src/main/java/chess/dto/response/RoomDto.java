package chess.dto.response;

import chess.domain.game.room.Room;

public class RoomDto {
    private final String id;
    private final String title;

    private RoomDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public static RoomDto from(Room room) {
        return new RoomDto(room.getId().getValue(), room.getRoomTitle().getValue());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

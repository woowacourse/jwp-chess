package chess.dto;

import java.util.List;

public class GameListDto {
    private final List<RoomDto> rooms;

    public GameListDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }
}

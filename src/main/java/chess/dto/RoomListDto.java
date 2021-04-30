package chess.dto;

import java.util.List;

public class RoomListDto {
    private final List<RoomDto> rooms;

    public RoomListDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }
}
